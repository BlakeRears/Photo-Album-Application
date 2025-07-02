/* BLAKE Rears
 * Alex Becerril
 */

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Album implements Serializable{
    
	
	private static final long serialVersionUID = 2L;
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";
	
    //counts numbers of photos per album
    public int numPhotos;
    
    //album name
    public String albumName;

    String dateString;
    
    public Photo currPhoto;

    //arraylist of photo objects
    public ArrayList<Photo> photoList;

    //constructor that takes album name
    public Album(String albumName) {
        this.albumName = albumName;
        this.photoList = new ArrayList<Photo>();
        numPhotos = 0;
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(currentDate);
    }


    //setters
    public void setName(String name) {
        this.albumName = name;
    }


    public Photo getCurrPhoto() {
        return this.currPhoto;
    }
    
    public void setCurrPhoto(int i) {
    	currPhoto = photoList.get(i);
    }
    
    //getters
    public int getPhotoCount() {
        return this.numPhotos;
    }
    public String getName() {
        return this.albumName;
    }
    public ArrayList<Photo> getPhotoList() {
        return this.photoList;
    }
    public void updatePhotos(ArrayList<Photo> photos) {
    	photoList.addAll(photos);
    	numPhotos++;
    }
    
    //add photo
    public void addPhoto(Photo photo) {
        this.photoList.add(photo);
        numPhotos++;
    }

    //delete photo
    public void deletePhoto(int photoIndex) {
        photoList.remove(photoIndex);
        numPhotos--;
    }
    
    //search for existing photo
    public boolean doesPhotoExist(String path) {
    	for(int i = 0; i < photoList.size(); i++) {
    		if(photoList.get(i).path.equals(path))
    			return true;
    	}
    	return false;
    }
    
    public String toString() {
        String myStr = String.format("%-70s %d %-1s %30s", albumName, numPhotos, "images", dateString);
    	return myStr;
    }
    public static void writeApp(Album album) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(album);
		oos.close();
	}
	
	public static User readApp() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User user = (User) ois.readObject();
		ois.close();
		return user;
	}

    

}
