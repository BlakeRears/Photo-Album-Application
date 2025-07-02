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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SlideshowController {
	
	Parent root;
	Stage stage;
	Scene scene;

	ArrayList<Photo> photos = new ArrayList<Photo>();
	Album album;
	User currUser;
	
	@FXML
	ImageView imageView;
	
	int currIndex;
	
	public void start(ArrayList<Photo> tempPhotos, User user) {
		currUser = user;
		photos.addAll(tempPhotos);
		
		if(!photos.isEmpty()) {
			currIndex = 0;
			File file = new File(photos.get(0).path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
	} 
	
	/*public void start(ArrayList<Photo> tempPhotos, User user) {
		this.user = user;
		photos.addAll(tempPhotos);
		
		if(!photos.isEmpty()) {
			currIndex = 0;
			File file = new File(photos.get(0).path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
	} */
	
	public void nextButton() {
		if(currIndex+1 < photos.size()) {
			currIndex++;
			File file = new File(photos.get(currIndex).path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
		else {
			 Alert alertMissingInfo = new Alert(AlertType.ERROR);
             alertMissingInfo.setTitle("Error!");
             alertMissingInfo.setHeaderText("NO PHOTOS AFTER THIS ONE");
             alertMissingInfo.setContentText("No photos occur after this one. You can't go forward.");
             alertMissingInfo.showAndWait();
             return;
		}
	}
	
	public void backButton() {
		if(currIndex-1 >= 0) {
			currIndex--;
			File file = new File(photos.get(currIndex).path);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
		}
		else {
			 Alert alertMissingInfo = new Alert(AlertType.ERROR);
             alertMissingInfo.setTitle("Error!");
             alertMissingInfo.setHeaderText("NO PHOTOS BEFORE THIS ONE");
             alertMissingInfo.setContentText("No photos occur before this one. You can't go back.");
             alertMissingInfo.showAndWait();
             return;
		}
	}
	
	public void exitButton(ActionEvent e) throws IOException {
	        
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));	
		 root = loader.load();
			
		 AlbumController albumController = loader.getController();
		 albumController.start(currUser); //PASS IN ANY VARIABLES YOU NEED INTO HERE
		 //albumController.start(user); //PASS IN ANY VARIABLES YOU NEED INTO HERE
		 stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		 scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
		 return;
	}
	
}
