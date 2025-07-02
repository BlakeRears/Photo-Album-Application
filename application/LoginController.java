/* BLAKE Rears
 * Alex Becerril
 */

package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class LoginController implements Initializable{

	ArrayList<String> users = new ArrayList<String>();
	String absolutePath;
	String username;
	
	@FXML
	TextField userText;
	
	private Stage stage;
	private Scene scene;
 	private Parent root;
 	
 	User currUser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		/*try {
			User.readApp();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} */
		 try {
	            String fileName = "data/usernames.txt";
	            Class<?> cls = Photos.class;
	            File file = new File(fileName);
	            absolutePath = file.getAbsolutePath();
	            if (file.createNewFile()) {
	                System.out.println("File created: " + file.getName());
	            } else {
	                System.out.println("File already exists.");
	            }
	        } catch (IOException e) {
	            System.out.println("An error occurred.");
	            e.printStackTrace();
	        }
	       try {
	    	   createListfromFile();
	     } catch (IOException e) {
	    	 e.printStackTrace();
	       }
 }
	
	public void start(User user) {
		currUser = user;
	}
	
	 public void loginButton(ActionEvent e) throws IOException {
		username = userText.getText();
		if(username.equalsIgnoreCase("admin")) {
			//SWITCH TO ADMIN SCENE
			//FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminPage.fxml"));	
			root = loader.load();
			
			AdminController adminController = loader.getController();
			adminController.start(users, absolutePath);
			
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			stage.setTitle("Admin");
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			return;
		}
		else if(username.equalsIgnoreCase("stock")) {
			User currUser = new User("stock");
					
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeScene.fxml"));	
			root = loader.load();
					
			HomeController homeController = loader.getController();
			homeController.start(currUser);		
			//start method should load up saved photos		
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			stage.setTitle("stock");
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			return;
		}
		else {
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).equals(username)) {
					//SWITCH TO THAT USERS SCENE
					User currUser = new User(username);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeScene.fxml"));	
					root = loader.load();
					
					HomeController homeController = loader.getController();
					homeController.start(currUser);
					
					
					stage = (Stage)((Node)e.getSource()).getScene().getWindow();
					stage.setTitle(username);
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
					return;
				}
			}
		}
		Alert alertMissingInfo = new Alert(AlertType.ERROR);
		alertMissingInfo.setTitle("Error!");
		alertMissingInfo.setHeaderText("USER DOES NOT EXIST");
		alertMissingInfo.setContentText("You have to enter a username that exist");
		alertMissingInfo.showAndWait();
		return;
	 }
	 
	 public void createListfromFile() throws IOException {
			
			File file = new File(absolutePath);
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                users.add(line);
	            }
	        } catch (IOException e) {
	            System.out.println("An error occurred while reading the file.");
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	 
}
