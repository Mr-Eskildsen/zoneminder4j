package name.eskildsen.zoneminder;

import java.net.URI;

public interface IZoneMinderResponse {
	
	URI getHttpRequestURI();
	String getHttpRequestUrl();
	int getHttpStatus();
	
    String getHttpResponseMessage();

}
