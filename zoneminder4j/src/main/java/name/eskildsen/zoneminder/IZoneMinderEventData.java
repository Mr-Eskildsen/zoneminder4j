package name.eskildsen.zoneminder;

public interface IZoneMinderEventData extends  IZoneMinderResponse {
	String getId();
	String getCause();
	String getStartTime(); 
	String getEndTime(); 
}
