package name.eskildsen.zoneminder.api;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public abstract class ZoneMinderData implements IZoneMinderResponse {
	
	private int responseCode = 0;
	private String responseMessage = "";
	public int getHttpResponseCode() {return responseCode;}
	public void setHttpResponseCode(int rc) {responseCode = rc;}
	
	public String  getHttpResponseMessage() {return responseMessage;}
	public void setHttpResponseMessage(String  rm) {responseMessage = rm;}

	public String getKey() {
		return this.getClass().getSimpleName();
	}	


}
