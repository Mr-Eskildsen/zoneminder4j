package name.eskildsen.zoneminder.api.monitor;


import java.io.ByteArrayOutputStream;
import java.net.URI;

import name.eskildsen.zoneminder.IMonitorDataStillImage;
import name.eskildsen.zoneminder.api.ZoneMinderCoreData;

public class ZoneMinderMonitorImage extends ZoneMinderCoreData implements IMonitorDataStillImage{
	private String id = "";
	private ByteArrayOutputStream image = null;
	
	//public ZoneMinderMonitorImage(String id, ByteArrayOutputStream image, int responseCode, String responseMessage, String requestUrl ) {
	//super(responseCode, responseMessage, requestUrl);
	public ZoneMinderMonitorImage(String id, int httpStatus, String httpMessage, URI httpURI) {
		super(httpStatus, httpMessage, httpURI);
		this.id = id;
	}
	
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ByteArrayOutputStream getImage() {
		return this.image;
	}
	@Override
	public void setImage(ByteArrayOutputStream image) {
		this.image = image;
	}
}