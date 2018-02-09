package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IZoneMinderHostVersion extends  IZoneMinderResponse {
    
	String getVersion();
    
	String getApiVersion();

}
