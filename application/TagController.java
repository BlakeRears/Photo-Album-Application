package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.AlbumController.PhotoListCell;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TagController {
	
	Parent root;
	Stage stage;
	Scene scene;

	Photo photo;
	User currUser;
	Album album;
	
	ArrayList<Photo> photos = new ArrayList<Photo>();
	
	ArrayList<Tag> tagsPreset = new ArrayList<Tag>();
	ArrayList<Tag> tagsPhoto = new ArrayList<Tag>();
	
	@FXML
	ImageView imageView;
	@FXML
    public ListView<Tag> listViewPresetTags;
	@FXML
    public ListView<Tag> listViewPhotoTags;
	
	public ObservableList<Tag> observePresetList;
	public ObservableList<Tag> observeTagList;
	
	/*public void start(User user, Photo photo, ArrayList<Photo> photos, Album album) {
		this.photos.addAll(photos);
		this.album = album;
		this.user = user;
		this.photo = photo;
		displayPhoto();
		if(!photo.tags.isEmpty()) {
			tagsPreset.addAll(photo.tags);
			updatePresetList();
            listViewPresetTags.getSelectionModel().select(0);
		}
	}*/
	
	public void start(User user) {
		currUser = user;
		this.photo = currUser.getCurrAlbum().getCurrPhoto();
		displayPhoto();
		if(!currUser.getCurrAlbum().getCurrPhoto().getTagList().isEmpty()) {
			tagsPhoto.addAll(currUser.getCurrAlbum().getCurrPhoto().getTagList());
			updateTagList();
            listViewPhotoTags.getSelectionModel().select(0);
		}
		if(!currUser.getPresetTagList().isEmpty()) {
			tagsPreset.addAll(currUser.getPresetTagList());
			updatePresetList();
            listViewPresetTags.getSelectionModel().select(0);
		}
	}
	
	public void displayPhoto() {
		if(photo != null) {
			File file = new File(photo.path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
	}
	
	public void backToAlbum(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));   
	    Parent root = loader.load();
	        
	    AlbumController albumController = loader.getController();
	    albumController.start(currUser);
	        
	    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
	}
	
	//STILL NEEDS TO SAVE PRESET TAGS FOR THIS USER TO A FILE
	public void addPresetTag() {
		TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Preset Tag");
        dialog.setHeaderText("Please enter a new preset tag (ex: Person Susan)");
        dialog.setContentText("Preset tag:");
        Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent()) {
	    	String[] str = result.get().split(" ");

                if (str[0].isEmpty() || str[0] == null || str.length != 2) {
                    Alert alertMissingInfo = new Alert(AlertType.ERROR);
                    alertMissingInfo.setTitle("Error!");
                    alertMissingInfo.setHeaderText("INVALID INPUT");
                    alertMissingInfo.setContentText("Tag is empty or invalid. Please enter a valid tag.");
                    alertMissingInfo.showAndWait();
                    return;
                }
                
                Tag tag = new Tag(str[1], str[0]);
                tagsPreset.add(tag);
                currUser.addPresetTag(tag);
                updatePresetList();
                listViewPresetTags.getSelectionModel().select(0);
	    	}
			return;
	}
	
	public void addTag() throws IOException {
		if(!tagsPreset.isEmpty()) {
			int selectedIndex = listViewPresetTags.getSelectionModel().getSelectedIndex();
			//if(!currUser.getCurrAlbum().getCurrPhoto().doesTagExist(tagsPreset.get(selectedIndex)) || (currUser.getCurrAlbum().getCurrPhoto().containsLocation() && tagsPreset.get(selectedIndex).getValueT().equalsIgnoreCase("Location"))) {
			if(!currUser.getCurrAlbum().getCurrPhoto().doesTagExist(tagsPreset.get(selectedIndex))) {	
				if(currUser.getCurrAlbum().getCurrPhoto().containsLocation() && tagsPreset.get(selectedIndex).getValueT().equalsIgnoreCase("Location")) {
					Alert alertMissingInfo = new Alert(AlertType.ERROR);
		            alertMissingInfo.setTitle("Error!");
		            alertMissingInfo.setHeaderText("NO MORE THAN ONE LOCATION IN LIST");
		            alertMissingInfo.setContentText("Can't add tag to photo because already has a location!");
		            alertMissingInfo.showAndWait();
		            return;
				}
				tagsPhoto.add(tagsPreset.get(selectedIndex));
				currUser.getCurrAlbum().getCurrPhoto().addTag(tagsPreset.get(selectedIndex)); //Saves to user
				updateTagList();
				listViewPhotoTags.getSelectionModel().select(0);
				Photo.writeApp(currUser.getCurrAlbum().getCurrPhoto());
			}
			else {
				Alert alertMissingInfo = new Alert(AlertType.ERROR);
	            alertMissingInfo.setTitle("Error!");
	            alertMissingInfo.setHeaderText("NO DUPLICATE TAGS IN LIST");
	            alertMissingInfo.setContentText("Can't add tag to photo because no photo already has this tag!");
	            alertMissingInfo.showAndWait();
	            return;
			}
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
            alertMissingInfo.setTitle("Error!");
            alertMissingInfo.setHeaderText("NO PRESET TAGS IN LIST");
            alertMissingInfo.setContentText("Can't add tag to photo because no tags have been added yet!");
            alertMissingInfo.showAndWait();
            return;
		}
	}
	
	//REMOVE TAG FROM CURRENT USERS CURRENT PHOTO
	public void removeTag() throws IOException {
		if(!tagsPhoto.isEmpty()) {
			int selectedIndex = listViewPhotoTags.getSelectionModel().getSelectedIndex();
			tagsPhoto.remove(tagsPhoto.get(selectedIndex));
			currUser.getCurrAlbum().getCurrPhoto().removeTag(selectedIndex); //Saves to user
			updateTagList();
			Photo.writeApp(currUser.getCurrAlbum().getCurrPhoto());
		}
		else {
			Alert alertMissingInfo = new Alert(AlertType.ERROR);
			alertMissingInfo.setTitle("Error!");
			alertMissingInfo.setHeaderText("NO TAGS IN LIST");
			alertMissingInfo.setContentText("Can't remove tag from photo because no tags have been added yet!");
			alertMissingInfo.showAndWait();
			return;
		}
	}
	
	public void updatePresetList() {
		ObservableList<Tag> observePresetList = FXCollections.observableArrayList(tagsPreset);
        listViewPresetTags.setItems(observePresetList);
	}
	
	//NEEDS TO UPDATE THE CURRENT USERS CURRENT PHOTO TAGS
	public void updateTagList() {
		ObservableList<Tag> observeTagList = FXCollections.observableArrayList(tagsPhoto);
        listViewPhotoTags.setItems(observeTagList);
	}

}
