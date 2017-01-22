package name.eskildsen.zoneminder;

public interface IZoneMinderHostVersion extends  IZoneMinderResponse {
    
	String getVersion();
    
	String getApiVersion();

}
