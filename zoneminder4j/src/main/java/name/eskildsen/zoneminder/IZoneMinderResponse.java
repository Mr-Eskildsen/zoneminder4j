package name.eskildsen.zoneminder;

public interface IZoneMinderResponse {

	String getHttpUrl();
	int getHttpResponseCode();
    String getHttpResponseMessage();

}
