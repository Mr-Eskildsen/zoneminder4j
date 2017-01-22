package name.eskildsen.zoneminder.exception;

import javax.ws.rs.core.UriBuilder;

public class ZoneMinderUrlNotFoundException extends Exception {
	public ZoneMinderUrlNotFoundException(UriBuilder uri) {
		super(String.format("Provided Url '%s' not a ZoneMinder Server", uri.toString()));
   	}
    public ZoneMinderUrlNotFoundException(String message) {
        super(message);
    }
}
