package name.eskildsen.zoneminder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.general.ZoneMinderPageTypeEnum;


public interface IZoneMinderCoreSession {
	
	IZoneMinderConnectionInfo getConnectionInfo();

	ZoneMinderPageTypeEnum getPortalPageType(); 
	
	boolean isAuthenticationEnabled();
	
	
	@Deprecated
	boolean isApiEnabled() throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;

}
