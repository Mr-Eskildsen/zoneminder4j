package name.eskildsen.zoneminder.general;

public class Tools {
	 public static String fixPath(String path) {
	        if (!path.startsWith("/")) {
	            path = "/" + path;
	        }
	        return path;

	    }
	    
}
