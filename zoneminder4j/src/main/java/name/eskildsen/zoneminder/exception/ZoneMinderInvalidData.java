package name.eskildsen.zoneminder.exception;

public class ZoneMinderInvalidData extends ZoneMinderException {
	
	public ZoneMinderInvalidData(String message, String _response, Throwable cause) {
		super(message, cause);
		response = _response;
	}
	
	
	private String response;
	
	public String getResponseString() {
		return response;
	}
	
}
