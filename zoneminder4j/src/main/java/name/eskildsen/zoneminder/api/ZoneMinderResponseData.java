package name.eskildsen.zoneminder.api;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public abstract class ZoneMinderResponseData implements IZoneMinderResponse {

	private String url = "";
	private int responseCode = 0;
	private String responseMessage = "";

	public String getHttpUrl() {return url;}
	public void setHttpUrl(String url) {this.url = url;}

	public int getHttpResponseCode() {return responseCode;}
	public void setHttpResponseCode(int rc) {responseCode = rc;}

	public String  getHttpResponseMessage() {return responseMessage;}
	public void setHttpResponseMessage(String  rm) {responseMessage = rm;}

	public String getKey() {
		return this.getClass().getSimpleName();
	}	


}
