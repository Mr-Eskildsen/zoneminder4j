package name.eskildsen.zoneminder.exception;


public class ZoneMinderAuthenticationException extends ZoneMinderException {
	
    public ZoneMinderAuthenticationException(String message ) {
        super(message, null);
    }

    public ZoneMinderAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
