package name.eskildsen.zoneminder;

import java.io.ByteArrayOutputStream;

public interface IMonitorDataStillImage  extends  IZoneMinderResponse {
	String getId();
	
	ByteArrayOutputStream getImage();

	void setImage(ByteArrayOutputStream image);
}
