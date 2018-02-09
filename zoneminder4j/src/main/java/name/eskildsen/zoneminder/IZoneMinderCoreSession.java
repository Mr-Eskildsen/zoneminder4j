package name.eskildsen.zoneminder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.common.ZoneMinderPageTypeEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;


public interface IZoneMinderCoreSession {
	
	IZoneMinderConnectionInfo getConnectionInfo();

	ZoneMinderPageTypeEnum getPortalPageType(); 
	
	boolean isAuthenticationEnabled();
	
	
	@Deprecated
	boolean isApiEnabled() throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;

}
