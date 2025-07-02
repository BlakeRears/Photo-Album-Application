/* BLAKE Rears
 * Alex Becerril
 */

package application;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchController {

    ArrayList<Album> arrayAlbumList;

    Parent root;
    Stage stage;
	Scene scene;
    
    @FXML
    public Button search;
    public Button exit;

    @FXML
    public TextField searchField;

    @FXML
    public ListView<Photo> photoListView;
    
	@FXML
    public VBox vbox;

    public ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
    public ArrayList<Album> AlbumList = new ArrayList<Album>();
    public ArrayList<Tag> tagList = new ArrayList<Tag>();

    public User currUser;

    public void start(User user) {
        currUser = user;  
        AlbumList.addAll(currUser.getAlbumArrayList());
    }

    public void readSearch(ActionEvent e) throws IOException {
        if (searchField.getText().trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blank Search");
			alert.setHeaderText("Search box not filled in");
			alert.setContentText("Type something into the search box");
        }

        String searchText = searchField.getText();
        

        if (searchText.contains(" AND ")) {
            String[] tag1ANDtag2 = searchText.split(" AND ");
            String tag1 = tag1ANDtag2[0];
            String tag2 = tag1ANDtag2[1];

            String[] nameValue1 = tag1.split("=");
            String[] nameValue2 = tag2.split("=");

            String value1 = nameValue1[0];
            String name1 = nameValue1[1];

            String value2 = nameValue2[0];
            String name2 = nameValue2[1];

            Tag firstTag = new Tag(name1, value1);
            Tag secondTag = new Tag(name2, value2);

            tagList.add(firstTag);
            tagList.add(secondTag);
            
            searchAND(tagList);

            
            //split it at and
            //split it at =
        }

        else if (searchText.contains(" OR ")) {
            String[] tag1ANDtag2 = searchText.split(" OR ");
            String tag1 = tag1ANDtag2[0];
            String tag2 = tag1ANDtag2[1];

            String[] nameValue1 = tag1.split("=");
            String[] nameValue2 = tag2.split("=");

            String value1 = nameValue1[0];
            String name1 = nameValue1[1];

            String value2 = nameValue2[0];
            String name2 = nameValue2[1];

            Tag firstTag = new Tag(name1, value1);
            Tag secondTag = new Tag(name2, value2);

            tagList.add(firstTag);
            tagList.add(secondTag);
            
            searchOR(tagList);
        }
        else if(!searchText.contains(" AND ") || !searchText.contains(" OR ")) {
            if (searchText.contains("=")) {
                String[] tagValue = searchText.split("=");
                String value = tagValue[0];
                String name = tagValue[1];
                Tag tag = new Tag(name, value);
                searchRegular(tag);
            }
            else if(searchText.contains(" TO ")) {
            	
            	String[] dates = searchText.split(" TO ");
            	String beforeDate = dates[0];
            	String afterDate = dates[1];
            	
            	dateSearch(beforeDate, afterDate);
            }
        }
        else {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setHeaderText("Please retype search query");
            alert.setContentText("Reminder: input and tags are case-sensitive!");
            return;
        }
    }

    public void dateSearch(String beforeDate, String afterDate) {
        this.photoArrayList.clear();
        photoArrayList.addAll(currUser.getPhotosWithinDateRange(beforeDate, afterDate));
        //display photos
        if(!photoArrayList.isEmpty()) {
        	updateList();
        }
    }

    public void searchRegular(Tag tag) {
        this.photoArrayList.clear();
        
        photoArrayList.addAll(currUser.getTaggedPhotos(tag));
        //displayPhotos()
        if(!photoArrayList.isEmpty()) {
        	updateList();
        }
    }

    public void searchAND(ArrayList<Tag> tagList) {
        this.photoArrayList.clear();
        Tag Tag1 = tagList.get(0);
        Tag Tag2 = tagList.get(1);
        photoArrayList.addAll(currUser.getTaggedPhotosAND(Tag1, Tag2));
        if(!photoArrayList.isEmpty()) {
        	updateList();
        }
    }

    public void searchOR(ArrayList<Tag> tagList) {
        this.photoArrayList.clear();
        Tag Tag1 = tagList.get(0);
        Tag Tag2 = tagList.get(1);
        ArrayList<Photo> photoArrayList1 = currUser.getTaggedPhotos(Tag1);
        ArrayList<Photo> photoArrayList2 = currUser.getTaggedPhotos(Tag2);
        photoArrayList.addAll(photoArrayList1);

        for (int i = 0; i < photoArrayList2.size(); i++) {
            if (!photoArrayList.contains(photoArrayList2.get(i))) {
                photoArrayList.add(photoArrayList2.get(i));
            }
        }
        if(!photoArrayList.isEmpty()) {
        	updateList();
        }
    }

    public void createAlbumFromSearch(ActionEvent e) throws IOException{
        if(photoArrayList.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Cannot Be Created");
            alert.setHeaderText("No Photos to Create Album");
            alert.setContentText("Please try again with a new search.");
            return;
       }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Album");
        dialog.setHeaderText("Please enter the name of the album:");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent()) {
            String name = result.get();

                if (name.isEmpty() || name == null) {
                    Alert alertMissingInfo = new Alert(AlertType.ERROR);
                    alertMissingInfo.setTitle("Error!");
                    alertMissingInfo.setHeaderText("EMPTY INPUT");
                    alertMissingInfo.setContentText("Album Name is missing. Please enter a name for the album.");
                    alertMissingInfo.showAndWait();
                    return;
                }

                for(int i = 0; i < currUser.getAlbumArrayList().size(); i++) {
	    			if(currUser.getAlbumArrayList().get(i).getName().equalsIgnoreCase(name)) {
	    			    Alert alertMissingInfo = new Alert(AlertType.ERROR);
	    				alertMissingInfo.setTitle("Error!");
	    				alertMissingInfo.setHeaderText("INVALID INPUT");
	    				alertMissingInfo.setContentText("Album already exist!");
	    				alertMissingInfo.showAndWait();
	    				return;
	    			}
	    		}
                
                //creates new album with that name
                Album newAlbum = new Album(name);

                //adds new album to user's list
                currUser.addAlbum(newAlbum);

                //adds all photos in photoArrayList to the album
                for(int i = 0; i < photoArrayList.size(); i++) {
                    newAlbum.addPhoto(photoArrayList.get(i));
                }
                
	    		updateList();
	    		User.writeApp(currUser);
	    		//saveUserInfo();
	    	}
			return;
    }
    public void RemovePhotoError(ActionEvent e) throws IOException {
        Alert alertMissingInfo = new Alert(AlertType.ERROR);
	    alertMissingInfo.setTitle("Error!");
	    alertMissingInfo.setHeaderText("Edit tags to remove photo");
	    alertMissingInfo.setContentText("Click function > tag to edit photos' tags");
	    alertMissingInfo.showAndWait();
	    return;
    }
    public void slideshow(ActionEvent e) throws IOException {
        if(!photoArrayList.isEmpty() ) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SlideshowPage.fxml"));	
			root = loader.load();
		
			SlideshowController slideshowController = loader.getController();
			slideshowController.start(photoArrayList, currUser);
			//slideshowController.start(photos, user);
		
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
    public void openPhoto(ActionEvent e) throws IOException {
        //switch to openPhoto with selected photo
        if(!photoArrayList.isEmpty()) {
			int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenPhotoPage.fxml"));	
			root = loader.load();
			Photo selectedPhoto = photoArrayList.get(selectedIndex);
			PhotoController photoController = loader.getController();
			//photoController.start(photos.get(selectedIndex), user); //PASS IN ANY VARIABLES YOU NEED INTO HERE
			photoController.start(selectedPhoto, currUser);
			
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
    public void exitSearch(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeScene.fxml"));	
		root = loader.load();
		HomeController homeController = loader.getController();
	    homeController.start(currUser);
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		stage.setTitle(currUser.getUsername());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
    }
    public void logout(ActionEvent e) throws IOException {
        //save Photos?
       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));	
        root = loader.load();
        LoginController loginController = loader.getController();
        loginController.start();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); */
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));   
	    Parent root = loader.load();
	        
	    LoginController loginController = loader.getController();
	    loginController.start(currUser);
	        
	    Stage stage = (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow();
	    stage.setScene(new Scene(root));
	    stage.show();
    }
    public void closeProgram(ActionEvent e) throws IOException {
        Platform.exit();
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
    public void tagButton(ActionEvent e) throws IOException {
		int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
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
    
    
    public void caption(ActionEvent e) throws IOException {
		if(photoArrayList.isEmpty()) {
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
	    
	    if (result.isPresent() && !photoArrayList.isEmpty()) {
            String caption = result.get();

                if (caption.isEmpty() || caption == null) {
                    Alert alertMissingInfo = new Alert(AlertType.ERROR);
                    alertMissingInfo.setTitle("Error!");
                    alertMissingInfo.setHeaderText("EMPTY INPUT");
                    alertMissingInfo.setContentText("Caption Name is missing. Please enter a caption for the photo.");
                    alertMissingInfo.showAndWait();
                    return;
                }
                
                int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
                photoArrayList.get(selectedIndex).changeCaption(caption);
                currUser.getCurrAlbum().getPhotoList().get(selectedIndex).changeCaption(caption); //Changes the users
                
	    		updateList();
	    	}
			return;
    }
	public void updateList() {
		 //user.getCurrAlbum().updatePhotos(photos);
		ObservableList<Photo> observePhotoList = FXCollections.observableArrayList(photoArrayList);
	    photoListView.setItems(observePhotoList);
	    photoListView.setCellFactory(param -> new PhotoListCell());
	}
	public void copyPhoto() {
		if(!photoArrayList.isEmpty() ) {
			int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
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
	
	public void movePhoto() {
		if(!photoArrayList.isEmpty() ) {
			int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
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
							photoArrayList.remove(selectedIndex);
							currUser.getCurrAlbum().getPhotoList().remove(selectedIndex); //Updates user
							currUser.getCurrAlbum().numPhotos--;
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
	}
	
	
	 private class PhotoListCell extends ListCell<Photo>{
		
        private final HBox hbox;
        private final ImageView imageViewSearch;
        private final Label captionField;
		
		public PhotoListCell() {
			
            super();
            hbox = new HBox();
            imageViewSearch = new ImageView();
            imageViewSearch.setFitHeight(100);
            imageViewSearch.setFitWidth(100);
            captionField = new Label();
            hbox.getChildren().addAll(imageViewSearch, captionField);
			
		}

        @Override
        protected void updateItem(Photo item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                Image image = new Image("file:" + item.path);
                imageViewSearch.setImage(image);
                captionField.setText(item.caption);
                captionField.setFont(captionField.getFont().font(20));
                captionField.setTranslateX(50);
                captionField.setTranslateY(35);
                setGraphic(hbox);
            }
        }
    }
	
}