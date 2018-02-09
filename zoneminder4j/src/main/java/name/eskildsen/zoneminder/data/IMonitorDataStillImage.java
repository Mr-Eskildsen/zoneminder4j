package name.eskildsen.zoneminder.data;

import java.io.ByteArrayOutputStream;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IMonitorDataStillImage  extends  IZoneMinderResponse {
	String getId();
	
	ByteArrayOutputStream getImage();

	void setImage(ByteArrayOutputStream image);
}
