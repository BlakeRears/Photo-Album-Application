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
import java.util.ArrayList;

public class User implements Serializable{
    public String username;
    public ArrayList<Album> albumArrayList;
    public ArrayList<Tag> presetTags;
    public Album currAlbum;
    
    private static final long serialVersionUID = 1L;
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";

    // Constructor
    public User (String username) {
        this.username = username;
        albumArrayList = new ArrayList<Album>();
        presetTags = new ArrayList<Tag>();
    }
    
    public ArrayList<Tag> getPresetTagList() {
    	return presetTags;
    }
    
    public void addPresetTag(Tag tag) {
    	presetTags.add(tag);
    }

    // Getters
    public String getUsername() {
        return this.username;
    }

    public ArrayList<Album> getAlbumArrayList() {
        return this.albumArrayList;
    }

    public Album getCurrAlbum() {
        return this.currAlbum;
    }
    public void setCurrAlbum(int i) {
    	currAlbum = albumArrayList.get(i);
    }

    public Album getAlbumAt(int index) {
		return albumArrayList.get(index);
	}



    // Setters
    public void setUsername(String username) {
		this.username = username;
	}

    public void setAlbumArrayList(ArrayList<Album> album) {
		this.albumArrayList = album;
	}

    public void addAlbum(Album album) {
        albumArrayList.add(album);
    }

    public void deleteAlbum(int index) {
		albumArrayList.remove(index);
	}
    public ArrayList<Photo> getTaggedPhotos(Tag tag) {
       
        ArrayList<Album> albumList = new ArrayList<Album>();
        ArrayList<Photo> photoList = new ArrayList<Photo>(); 
        ArrayList<Photo> photosWithTag = new ArrayList<Photo>();
        
        albumList.addAll(this.albumArrayList);
        
        for (int i = 0; i < albumList.size(); i++) {
        	photoList.addAll(albumList.get(i).getPhotoList());
            for (int j = 0; j < photoList.size(); j++) {
                Photo temp = photoList.get(j);
                boolean tagged = temp.doesTagExist(tag);
                if (tagged == true) {
                    photosWithTag.add(temp);
                }
            }
        }
        return photosWithTag;
    }
    
     public ArrayList<Photo> getTaggedPhotosAND(Tag tag1, Tag tag2) {
    	 
    	ArrayList<Album> albumList = new ArrayList<Album>();
        ArrayList<Photo> photoList = new ArrayList<Photo>(); 
        ArrayList<Photo> photosWithTag = new ArrayList<Photo>();
        
        
        albumList.addAll(this.albumArrayList);
        for (int i = 0; i < albumList.size(); i++) {
            photoList.addAll(albumList.get(i).getPhotoList());
            for (int j = 0; j < photoList.size(); j++) {
                Photo temp = photoList.get(j);
                boolean tagged = temp.checkForTag(tag1);
                boolean tagged2 = temp.checkForTag(tag2);
                if (tagged == true || tagged2 == true) {
                    photosWithTag.add(temp);
                }
            }
        }
        return photosWithTag;
    }
    public ArrayList<Photo> getPhotosWithinDateRange(String beforeDate, String afterDate) {
    	 
    	 ArrayList<Photo> photosWithinDate = new ArrayList<Photo>();
    	 //ArrayList<Album> albumList = this.albumArrayList;
    	 ArrayList<Album> albumList = new  ArrayList<Album>();
         ArrayList<Photo> photoList = new ArrayList<Photo>(); 
    	 
         albumList.addAll(this.albumArrayList);
         
         for (int i = 0; i < albumList.size(); i++) {
             photoList.addAll(albumList.get(i).getPhotoList());
             for (int j = 0; j < photoList.size(); j++) {
                 Photo temp = photoList.get(j);
                // System.out.println(temp.dateString);
                 boolean checkDateRange = temp.checkForDate(beforeDate, afterDate); 
                 if (checkDateRange) {
                     photosWithinDate.add(temp);
                 }
             }
         }
         
    	 return photosWithinDate;
     }
    public static void writeApp(User user) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(user);
		oos.close();
	}
	
	public static User readApp() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User user = (User) ois.readObject();
		ois.close();
		return user;
	}

    

}
