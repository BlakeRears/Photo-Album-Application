package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PhotoController {

	@FXML
	ImageView imageView;
	
	@FXML
	Label captionLabel;
	
	@FXML
	Label dateLabel;
	
	@FXML
	Label tagLabel;
	
	Album album;
	ArrayList<Photo> photos = new ArrayList<Photo>();
	User currUser;
	
	Photo photo;
	
	Parent root;
	Stage stage;
	Scene scene;
	
	/*public void start(Album album, ArrayList<Photo> photos, Photo photo) {
		this.album = album;
		this.photos.addAll(photos);
		this.photo = photo;
		displayPhoto();
	} */
	
	public void start(int index, User user) {
		currUser = user;
		photo = currUser.getCurrAlbum().getPhotoList().get(index);
		displayPhoto();
	} 
	
	public void start(Photo selectedPhoto, User user) {
		currUser = user;
		photo = selectedPhoto;
		displayPhoto();
	} 
	
	public void displayPhoto() {
		if(photo != null) {
			File file = new File(photo.path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
			captionLabel.setText(photo.caption);
			dateLabel.setText(photo.dateString);
			if(currUser.getCurrAlbum().getCurrPhoto().getTagList().isEmpty()) {
				tagLabel.setText("N/A"); //NEEDS TO BE CHANGED
			}
			else {
				String str = "";
				for(int i = 0; i < currUser.getCurrAlbum().getCurrPhoto().getTagList().size(); i++) {
					str += currUser.getCurrAlbum().getCurrPhoto().getTagList().get(i) + " ";
				}
				tagLabel.setText(str);
			}
		}
	}
	
	public void exitPhoto(ActionEvent e) throws IOException {
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));	
		 root = loader.load();
			
		 AlbumController albumController = loader.getController();
		// albumController.start(album, photos); //PASS IN ANY VARIABLES YOU NEED INTO HERE
		 albumController.start(currUser);
		 stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		 scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();

		 return;
	}
} 
