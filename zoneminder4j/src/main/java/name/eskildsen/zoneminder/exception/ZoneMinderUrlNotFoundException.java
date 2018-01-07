package name.eskildsen.zoneminder.exception;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class ZoneMinderUrlNotFoundException extends Exception {
	public ZoneMinderUrlNotFoundException(URI uriLogin) {
		super(String.format("Provided Url '%s' not a ZoneMinder Server", uriLogin.toString()));
   	}
    public ZoneMinderUrlNotFoundException(String message) {
        super(message);
    }
}
