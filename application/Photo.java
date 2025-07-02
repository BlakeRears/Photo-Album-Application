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
import java.util.Calendar;
import java.util.Date;

public class Photo implements Serializable{
	
	private static final long serialVersionUID = 3L;
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";
	
	 //filepath
    public String path;

    //caption
    public String caption;

    //tags
    public ArrayList<Tag> tags;

    //picture file
    public File picture;

    //name
    public String name;

    //calendar
    public String dateString;

    //user
    public String user;
	
	public Photo(String path) {
		this.path = path;
		caption = "N/A";
		 Date currentDate = new Date();
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	     dateString = formatter.format(currentDate);
	     tags = new ArrayList<Tag>();
	}
	
	 public boolean doesTagExist(Tag tag) {
	    	for(int i = 0; i < tags.size(); i++) {
	    		if(tags.get(i).getNameT().equals(tag.getNameT()))
	    			return true;
	    	}
	    	return false;
	    } 
	 
	 public boolean containsLocation() {
		 for(int i = 0; i < tags.size(); i++) {
			 if(tags.get(i).getValueT().equalsIgnoreCase("Location")) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	public ArrayList<Tag> getTagList() {
    	return tags;
    }
	
	public void removeTag(int i) {
		tags.remove(i);
	}
    
    public void addTag(Tag tag) {
    	tags.add(tag);
    }
	
	public void changeCaption(String str) {
		caption = str;
	}
	public boolean checkForTag(Tag tag) {
        boolean tagged = false;
        for (int i = 0; i < this.tags.size(); i++) {
            if (tag == tags.get(i)) {
                tagged = true;
            }
        }
        return tagged; 
    }
   /* public boolean checkForTag(Tag tag) {
        boolean tagged = false;
        for (int i = 0; i < this.tags.size(); i++) {
            if (tag == tags.get(i)) {
                tagged = true;
            }
        }
        return tagged; 
    } */
	
	public boolean checkForDate(String givenBeforeDate, String givenAfterDate) {
        boolean result = false;
        String[] ymd = givenAfterDate.split("-");
        String yearAfter = ymd[0];
        String monthAfter = ymd[1];
        String dayAfter = ymd[2];
        
        int yearAfterInt = Integer.parseInt(yearAfter);
        int monthAfterInt = Integer.parseInt(monthAfter);
        int dateAfterInt = Integer.parseInt(dayAfter);
        
        String[] ymdBefore = givenBeforeDate.split("-");
        String yearBefore = ymdBefore[0];
        String monthBefore = ymdBefore[1];
        String dayBefore = ymdBefore[2];
        
        int yearBeforeInt = Integer.parseInt(yearBefore);
        int monthBeforeInt = Integer.parseInt(monthBefore);
        int dateBeforeInt = Integer.parseInt(dayBefore);
        
        String[] ymdPhoto = this.dateString.split("-");
        String yearPhoto = ymdPhoto[0];
        String monthPhoto = ymdPhoto[1];
        String dayPhoto = ymdPhoto[2];
        
        int yearPhotoInt = Integer.parseInt(yearPhoto);
        int monthPhotoInt = Integer.parseInt(monthPhoto);
        int datePhotoInt = Integer.parseInt(dayPhoto);
        
        if(yearPhotoInt <= yearAfterInt && yearPhotoInt >= yearBeforeInt) {
        	if (monthPhotoInt <= monthAfterInt && monthPhotoInt >= monthBeforeInt) {
        		if (datePhotoInt <= dateAfterInt && datePhotoInt >= dateBeforeInt) {
        			result = true;
        		}
        	}
        }
        return result;
    }
	
	public static void writeApp(Photo photo) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(photo);
		oos.close();
	}
	
	public static User readApp() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User user = (User) ois.readObject();
		ois.close();
		return user;
	}
    
}
