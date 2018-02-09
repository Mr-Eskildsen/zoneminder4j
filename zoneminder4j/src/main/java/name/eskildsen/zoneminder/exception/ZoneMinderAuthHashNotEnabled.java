package name.eskildsen.zoneminder.exception;

public class ZoneMinderAuthHashNotEnabled extends ZoneMinderException  {

	public ZoneMinderAuthHashNotEnabled(String message) {
		super(message, null);
	}
	
	public ZoneMinderAuthHashNotEnabled() {
		super("", null);

	}
}
