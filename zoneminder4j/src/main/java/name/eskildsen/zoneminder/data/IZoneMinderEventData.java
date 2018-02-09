package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IZoneMinderEventData extends  IZoneMinderResponse {
	String getId();
	String getCause();
	String getStartTime(); 
	String getEndTime(); 
}
