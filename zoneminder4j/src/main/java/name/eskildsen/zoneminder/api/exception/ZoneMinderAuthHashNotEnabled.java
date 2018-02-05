package name.eskildsen.zoneminder.api.exception;

import name.eskildsen.zoneminder.exception.ZoneMinderException;

public class ZoneMinderAuthHashNotEnabled extends ZoneMinderException  {

	public ZoneMinderAuthHashNotEnabled(String message) {
		super(message, null);
	}
	
	public ZoneMinderAuthHashNotEnabled() {
		super("", null);

	}
}
