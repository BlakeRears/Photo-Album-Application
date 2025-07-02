/* BLAKE Rears
 * Alex Becerril
 */

package application;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class HomeController implements Initializable{
	Parent root;
    Stage stage;
	Scene scene;

    @FXML
	Button createAlbum;
	@FXML
	Button openAlbum;
	@FXML
	Button renameAlbum;
	@FXML
	Button deleteAlbum;
	@FXML
	Button search;
    @FXML
    public ListView<Album> listviewAlbum;
    
    public ObservableList<Album> observeAlbumList;
    
    User currUser;
    //public static ArrayList<Album> arrayAlbumList = new ArrayList<Album>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //CODE TO CHECK IF USER HAS ANY ALBUMS AND IF SO STORE THEM IN THE ARRAYLIST AND SET THE LIST EQUAL TO THAT
    }
    
    public void start(User user) {
    	
    	currUser = user;
    	if (currUser.getUsername().equals("stock")) {
    		//load up stock photo list
    		//switch to open album scene
    		File stockPhotosFolder = new File("stockPhotos");
    		Album stockAlbum = new Album("stock");
    		if (stockPhotosFolder.exists() && stockPhotosFolder.isDirectory()) {
    			 File[] stockPhotos = stockPhotosFolder.listFiles();
    			 if (stockPhotos != null) {
    				 for (int i = 0; i < stockPhotos.length; i++) {
    					 if(stockPhotos[i].isFile()) {
    						 Photo temp = new Photo(stockPhotos[i].getAbsolutePath());
    						 temp.changeCaption("Photo " + i);
    						 stockAlbum.addPhoto(temp);
    					 }
    				 }
    			 }
    		}
    		if(currUser.getAlbumArrayList().isEmpty()) {
    			currUser.addAlbum(stockAlbum);
    		}
    			currUser.setCurrAlbum(0);
    	}
    	
    	if(!currUser.getAlbumArrayList().isEmpty()) {
    		updateList();
    	}
    	
    }
    
    public void openStockAlbum(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));	
		root = loader.load();
		
		AlbumController albumController = loader.getController();
		albumController.start(currUser); //PASS IN ANY VARIABLES YOU NEED INTO HERE
		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;

        
    }

    public void createAlbum() throws IOException {
        //GO THROUGH SPECIFIC USERS TO SEE
        //So make a User Object, and store the three lists in there
        //that way if you want to access an album, you have to check for the specific user first
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
                
                Album newAlbum = new Album(name);
                currUser.addAlbum(newAlbum);
	    		updateList();
	    		User.writeApp(currUser);
	    		//saveUserInfo();
	    	}
			return;
    }

    public void renameAlbum() throws IOException {
        //GO THROUGH SPECIFIC USERS TO SEE
        //So make a User Object, and store the three lists in there
        //that way if you want to access an album, you have to check for the specific user first
        int indexAlbum = listviewAlbum.getSelectionModel().getSelectedIndex();
        Album selectedAlbum = currUser.getAlbumAt(indexAlbum); 

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Please enter a new name for the album:");
        dialog.setContentText("New Name:");
        Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent()) {
            String name = result.get();
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
                if (name.isEmpty() || name == null) {
                    Alert alertMissingInfo = new Alert(AlertType.ERROR);
                    alertMissingInfo.setTitle("Error!");
                    alertMissingInfo.setHeaderText("EMPTY INPUT");
                    alertMissingInfo.setContentText("Album Name is missing. Please enter a name for the album.");
                    alertMissingInfo.showAndWait();
                    return;
                }
            selectedAlbum.setName(name);
	        updateList();
	        User.writeApp(currUser);
	    		
            
        }
        return;
    }

    public void openAlbum(ActionEvent e) throws IOException {
        //GO THROUGH SPECIFIC USERS TO SEE
        //So make a User Object, and store the three lists in there
        //that way if you want to access an album, you have to check for the specific user first
        int albumIndex = listviewAlbum.getSelectionModel().getSelectedIndex();
        //Album selectedAlbum = currUser.getAlbumAt(albumIndex);
        currUser.setCurrAlbum(albumIndex);
        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));	
		root = loader.load();
		
		AlbumController albumController = loader.getController();
		albumController.start(currUser); //PASS IN ANY VARIABLES YOU NEED INTO HERE
		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;

        
    }

    public void deleteAlbum() throws IOException {
        int selectedIndex = listviewAlbum.getSelectionModel().getSelectedIndex();
        observeAlbumList.remove(listviewAlbum.getSelectionModel().getSelectedItem());
        currUser.deleteAlbum(selectedIndex);
        updateList();
        User.writeApp(currUser);
    }

    public void updateList() {
        //currUser.getAlbumArrayList().sort(new AlbumComparator());
        observeAlbumList = FXCollections.observableArrayList(currUser.getAlbumArrayList());
        listviewAlbum.setItems(observeAlbumList);
        listviewAlbum.refresh();
        listviewAlbum.getSelectionModel().selectFirst();
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

}