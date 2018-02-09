package name.eskildsen.zoneminder.internal;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;

public interface IZoneMinderConnectionInfoInternal extends IZoneMinderConnectionInfo {
	
	//TODO:: MOVE TO PROXY !!!
	public String getHttpUrl();
	public int getResponseCode(); 
	public String getResponseMessage();

}
