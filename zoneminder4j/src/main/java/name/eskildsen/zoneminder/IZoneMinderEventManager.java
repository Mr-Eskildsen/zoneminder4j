package name.eskildsen.zoneminder;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public interface IZoneMinderEventManager {
	
	boolean UpdateConnection(IZoneMinderConnectionInfo connection) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;

	boolean validateConnection(IZoneMinderConnectionInfo connection);
	
	boolean validateLogin(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;
	boolean isApiEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;
	boolean isTriggerOptionEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;
	
	void setConnected(boolean newState);
	boolean isConnected();
	
	void SubscribeMonitorEvents(IZoneMinderConnectionInfo connection, String monitorId, IZoneMinderEventSubscriber subscriber)  throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException;
	void UnsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber);

}
