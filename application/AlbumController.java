/* BLAKE Rears
 * Alex Becerril
 */

package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AlbumController {
	
	Parent root;
	Stage stage;
	Scene scene;

	Album album;
	ArrayList<Photo> photos = new ArrayList<Photo>();
	User currUser;
	
	@FXML
    public ListView<Photo> listView;
	@FXML
    public VBox vbox;
	
	public ObservableList<Photo> observePhotoList;
	
	 /*public void start(Album album) { 
		this.album = album;
		//CODE TO LOAD ALL PHOTOS FROM ALBUM
		photos.addAll(album.photoList);
		
		if(!photos.isEmpty()) {
			updateList();
			listView.getSelectionModel().select(0);
		}
	} */
	
	public void start(User user) {
		currUser = user;
		album = user.getCurrAlbum();
		photos.addAll(album.photoList);
		if(!photos.isEmpty()) {
			updateList();
			listView.getSelectionModel().select(0);
		}
	}
	
	/*public void start(Album album, ArrayList<Photo> tempPhotos) {
		this.album = album;
		//CODE TO LOAD ALL PHOTOS FROM ALBUM
		photos.addAll(tempPhotos);
		
		if(!photos.isEmpty()) {
			updateList();
			listView.getSelectionModel().select(0);
		} 
		
	}*/
	
	public void openPhoto(ActionEvent e) throws IOException {
		if(!photos.isEmpty()) {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			currUser.getCurrAlbum().setCurrPhoto(selectedIndex);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenPhotoPage.fxml"));	
			root = loader.load();
				
			PhotoController photoController = loader.getController();
			photoController.start(selectedIndex, currUser); //PASS IN ANY VARIABLES YOU NEED INTO HERE
			//photoController.start(album, photos, photos.get(selectedIndex));
			
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			return;
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
            alertMissingInfo.setContentText("Can't open photo because none have been added yet!");
            alertMissingInfo.showAndWait();
            return;
		}
	}
	
	public void addPhoto() throws IOException {

		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String photoPath = selectedFile.getAbsolutePath();
            Photo photo = new Photo(photoPath);
            photos.add(photo);
            currUser.currAlbum.addPhoto(photo); //Updates users album
            ObservableList<Photo> observePhotoList = FXCollections.observableArrayList(photos);
            listView.setItems(observePhotoList);
            listView.setCellFactory(param -> new PhotoListCell());
            listView.getSelectionModel().select(0);
            Album.writeApp(currUser.getCurrAlbum()); //FILE WRITE

        }
    }
	
	public void updateList() {
		 //user.getCurrAlbum().updatePhotos(photos);
		 ObservableList<Photo> observePhotoList = FXCollections.observableArrayList(photos);
         listView.setItems(observePhotoList);
         listView.setCellFactory(param -> new PhotoListCell());
	}

	
	public void deletePhoto() throws IOException {
		if(!photos.isEmpty()) {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			photos.remove(selectedIndex);
			currUser.getCurrAlbum().getPhotoList().remove(selectedIndex); //Updates user
			currUser.getCurrAlbum().numPhotos--;
			updateList();
			Album.writeApp(currUser.getCurrAlbum()); //WRITE FILE
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
            alertMissingInfo.setContentText("Can't delete a photo because none have been added yet!");
            alertMissingInfo.showAndWait();
            return;
		}
    }
	
	public void caption() throws IOException {
		
		if(photos.isEmpty()) {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
	        alertMissingInfo.setTitle("Error!");
	        alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
	        alertMissingInfo.setContentText("Can't caption a photo because none have been added yet!");
	        alertMissingInfo.showAndWait();
	        return;
		}
		TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Caption");
        dialog.setHeaderText("Please enter the caption for the photo:");
        dialog.setContentText("Caption:");
        Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent() && !photos.isEmpty()) {
            String caption = result.get();

                if (caption.isEmpty() || caption == null) {
                    Alert alertMissingInfo = new Alert(AlertType.ERROR);
                    alertMissingInfo.setTitle("Error!");
                    alertMissingInfo.setHeaderText("EMPTY INPUT");
                    alertMissingInfo.setContentText("Caption Name is missing. Please enter a caption for the photo.");
                    alertMissingInfo.showAndWait();
                    return;
                }
                
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                photos.get(selectedIndex).changeCaption(caption);
                currUser.getCurrAlbum().getPhotoList().get(selectedIndex).changeCaption(caption); //Changes the users
                
	    		updateList();
	    		Photo.writeApp(currUser.getCurrAlbum().getPhotoList().get(selectedIndex));
	    	}
			return;
    }
	
	public void slideShow(ActionEvent e) throws IOException {
		if(!photos.isEmpty() ) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SlideshowPage.fxml"));	
			root = loader.load();
		
			SlideshowController slideshowController = loader.getController();
			//slideshowController.start(photos, album);
			slideshowController.start(photos, currUser);
		
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
            alertMissingInfo.setContentText("Can't slideshow with no photos!");
            alertMissingInfo.showAndWait();
            return;
		}
		
	}
	
	public void tagButton(ActionEvent e) throws IOException {
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		currUser.getCurrAlbum().setCurrPhoto(selectedIndex);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tags.fxml"));   
	    Parent root = loader.load();
	        
	    TagController tagController = loader.getController();
	    //tagController.start(currUser, photos.get(selectedIndex), photos, album);
	    tagController.start(currUser);
	        
	    Stage stage = (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow();
	    stage.setScene(new Scene(root));
	    stage.show();
	}
	
	public void backToHome(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeScene.fxml"));   
	    Parent root = loader.load();
	        
	    HomeController homeController = loader.getController();
	    homeController.start(currUser);
	        
	    Stage stage = (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow();
	    stage.setScene(new Scene(root));
	    stage.show();
	}
	
	public void copyPhoto() throws IOException {
		if(!photos.isEmpty() ) {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			Photo photo = currUser.getCurrAlbum().getPhotoList().get(selectedIndex);
		
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Copy Photo");
			dialog.setHeaderText("Please enter the album you'd like to copy the photo to");
			dialog.setContentText("Album name:");
			Optional<String> result = dialog.showAndWait();
	    
			if (result.isPresent()) {
				String albumName = result.get();
				for(int i = 0; i < currUser.getAlbumArrayList().size(); i++) {
					if(currUser.getAlbumArrayList().get(i).albumName.equalsIgnoreCase(albumName)) {
						boolean photoExists = false;
						for(int j = 0; j < currUser.getAlbumArrayList().get(i).getPhotoList().size(); j++) {
							if (currUser.getAlbumArrayList().get(i).getPhotoList().get(j).equals(photo)) {
								photoExists = true;
							}
						}
						if (!photoExists) {
							currUser.getAlbumArrayList().get(i).addPhoto(photo);
							Album.writeApp(currUser.getCurrAlbum());
						}
						return;
					}
				}
				Alert alertMissingInfo = new Alert(AlertType.ERROR);
	            alertMissingInfo.setTitle("Error!");
	            alertMissingInfo.setHeaderText("ALBUM NOT FOUND");
	            alertMissingInfo.setContentText("Please enter an album name that exist!");
	            alertMissingInfo.showAndWait();
	            return;
			}
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
            alertMissingInfo.setContentText("Can't copy with no photos!");
            alertMissingInfo.showAndWait();
            return;
		}
	}
	
	public void movePhoto() throws IOException {
		if(!photos.isEmpty() ) {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			Photo photo = currUser.getCurrAlbum().getPhotoList().get(selectedIndex);
		
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Move Photo");
			dialog.setHeaderText("Please enter the album you'd like to move the photo to");
			dialog.setContentText("Album name:");
			Optional<String> result = dialog.showAndWait();
	    
			if (result.isPresent()) {
				String albumName = result.get();
				for(int i = 0; i < currUser.getAlbumArrayList().size(); i++) {
					if(currUser.getAlbumArrayList().get(i).albumName.equalsIgnoreCase(albumName)) {
						boolean photoExists = false;
						for(int j = 0; j < currUser.getAlbumArrayList().get(i).getPhotoList().size(); j++) {
							if (currUser.getAlbumArrayList().get(i).getPhotoList().get(j).equals(photo)) {
								photoExists = true;
							}
						}
						if (!photoExists) {
							currUser.getAlbumArrayList().get(i).addPhoto(photo);
							photos.remove(selectedIndex);
							currUser.getCurrAlbum().getPhotoList().remove(selectedIndex); //Updates user
							currUser.getCurrAlbum().numPhotos--;
							Album.writeApp(currUser.getCurrAlbum());
							updateList();
						}
						return;
					}
				}
				Alert alertMissingInfo = new Alert(AlertType.ERROR);
	            alertMissingInfo.setTitle("Error!");
	            alertMissingInfo.setHeaderText("ALBUM NOT FOUND");
	            alertMissingInfo.setContentText("Please enter an album name that exist!");
	            alertMissingInfo.showAndWait();
	            return;
			}
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PHOTOS IN LIST");
            alertMissingInfo.setContentText("Can't copy with no photos!");
            alertMissingInfo.showAndWait();
            return;
		}
	}
	
	public void logOut(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));   
	    Parent root = loader.load();
	        
	    LoginController loginController = loader.getController();
	    loginController.start(currUser);
	        
	    Stage stage = (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow();
	    stage.setTitle("Log in");
	    stage.setScene(new Scene(root));
	    stage.show();
	}
	
	public void closeProgram() {
        Platform.exit();
    }
	
	 public static class PhotoListCell extends ListCell<Photo> {
	        private final HBox hbox;
	        private final ImageView imageView;
	        private final Label captionField;

	        public PhotoListCell() {
	            super();
	            hbox = new HBox();
	            imageView = new ImageView();
	            imageView.setFitHeight(100);
	            imageView.setFitWidth(100);
	            captionField = new Label();
	            hbox.getChildren().addAll(imageView, captionField);
	        }

	        @Override
	        protected void updateItem(Photo item, boolean empty) {
	            super.updateItem(item, empty);

	            if (empty || item == null) {
	                setGraphic(null);
	                setText(null);
	            } else {
	                Image image = new Image("file:" + item.path);
	                imageView.setImage(image);
	                captionField.setText(item.caption);
	                captionField.setFont(captionField.getFont().font(20));
	                captionField.setTranslateX(50);
	                captionField.setTranslateY(35);
	                setGraphic(hbox);
	            }
	        }
	    }
	 
	 public void search(ActionEvent e) throws IOException {

	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchScene.fxml"));	
			root = loader.load();
			
			SearchController searchController = loader.getController();
			searchController.start(currUser); //PASS IN ANY VARIABLES YOU NEED INTO HERE
			
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			return;
	    }
}
