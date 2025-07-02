/* BLAKE Rears
 * Alex Becerril
 */

package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AdminController {

	Parent root;
	Stage stage;
	Scene scene;
	
	User currUser;
	
	String absolutePath;
	
	@FXML
	ListView<String> listview;
	
	private ArrayList<String> users = new ArrayList<String>();
	public ObservableList<String> observableUsersList;
	int selectedIndex;
	
	public void start(ArrayList<String> info, String path) {
		absolutePath = path;
		
		users.addAll(info);
		
		 users.sort(new UsersComparator());
		 observableUsersList = FXCollections.observableArrayList(users);

	     listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
             public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                 try {
            
                 }
                 catch(NullPointerException e) {

                 }

             }
         });
	        listview.setItems(observableUsersList); //sets up list to be viewed
	        listview.getSelectionModel().select(0); //auto select first of the list
	}
	
	public void logOut(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));	
		root = loader.load();
		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		stage.setTitle("Log in");
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void addUser() throws IOException {
		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Input Dialog");
	    dialog.setHeaderText("Please enter your name:");
	    dialog.setContentText("Name:");
	    Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent()) {
	    	String name = result.get();
	    	if(name.equals("Admin") || name.equals("Stock")) {
	    		Alert alertMissingInfo = new Alert(AlertType.ERROR);
	    		alertMissingInfo.setTitle("Error!");
	    		alertMissingInfo.setHeaderText("INVALID INPUT");
	    		alertMissingInfo.setContentText("Username can not be (Admin) or (Stock)");
	    		alertMissingInfo.showAndWait();
	    		return;
	    	}
	    	else {
	    		for(int i = 0; i < users.size(); i++) {
	    			if(users.get(i).equalsIgnoreCase(name)) {
	    				Alert alertMissingInfo = new Alert(AlertType.ERROR);
	    				alertMissingInfo.setTitle("Error!");
	    				alertMissingInfo.setHeaderText("INVALID INPUT");
	    				alertMissingInfo.setContentText("Username already exist!");
	    				alertMissingInfo.showAndWait();
	    				return;
	    			}
	    		}
	    		users.add(name);
	    		User newUser = new User(name);
	    		updateList();
	    		saveUserInfo();
	    	}
	    }
	    else {
	    	return;
	    }
	}
	
	public void deleteUserButton() {
		if (observableUsersList.isEmpty()) {
			Alert alertNoInteger = new Alert(AlertType.ERROR);
			alertNoInteger.setTitle("Error!");
			alertNoInteger.setHeaderText("There are no users to delete from this list.");
			alertNoInteger.setContentText("Add users to the list before deleting.");
			alertNoInteger.showAndWait();
			return;
		}
		Alert alertConfirmDelete = new Alert(AlertType.CONFIRMATION);
		alertConfirmDelete.setTitle("Confirmation");
		alertConfirmDelete.setHeaderText("Are you sure you would like to delete this user from list?");
		alertConfirmDelete.setContentText("Click OK to confirm");
		Optional<ButtonType> result = alertConfirmDelete.showAndWait();
		
		if(result.isEmpty()) {
			return;
		}
		else if(result.get() == ButtonType.CANCEL) {
			return;
		}
		else if(result.get() == ButtonType.OK){
			removeUser();
		}
}
	
	public void removeUser() {
		selectedIndex = listview.getSelectionModel().getSelectedIndex();
		observableUsersList.remove(listview.getSelectionModel().getSelectedItem());
		users.remove(selectedIndex);
		updateList();
		saveUserInfo();
		
	}
	
	public void saveUserInfo() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath))) {
            for (String name : users) {
                writer.write(name);
                writer.newLine();
            }
            System.out.println("File written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void updateList() {
		 users.sort(new UsersComparator());
	     observableUsersList = FXCollections.observableArrayList(users);
	     listview.setItems(observableUsersList);
	     listview.refresh();
	     listview.getSelectionModel().selectFirst();
	}
}


