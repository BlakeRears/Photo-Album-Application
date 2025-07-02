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

public class Tag implements Serializable{
	
	private static final long serialVersionUID = 4L;
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";
	
    public String nameT;
    public String valueT;

    public Tag(String nameT, String valueT) {
        this.nameT = nameT;
        this.valueT = valueT;
    }

	//setters
	public void setNameT(String name) {
		this.nameT = name;
	}
	
	public void setValueT(String value) {
		this.valueT = value;
	}
	//getters
	public String getNameT() {
		return this.nameT;
	}
	
	public String getValueT() {
		return this.valueT;
	}
	
	public String toString() {
		return nameT;
	}
	public static void writeApp(Tag tag) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(tag);
		oos.close();
	}
	
	public static User readApp() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User user = (User) ois.readObject();
		ois.close();
		return user;
	}
}
