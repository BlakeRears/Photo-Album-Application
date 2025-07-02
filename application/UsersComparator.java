package application;

import java.util.Comparator;

	public class UsersComparator implements Comparator<String> {
        public int compare(String first, String second) {
            int i = first.toLowerCase().compareTo(second.toLowerCase());
            if (i == 0) {
                i = first.toLowerCase().compareTo(second.toLowerCase());
            }
            return i;
        }
}