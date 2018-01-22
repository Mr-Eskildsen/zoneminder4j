package name.eskildsen.zoneminder;


import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public interface IZoneMinderEventManager {
	
	boolean UpdateConnection(IZoneMinderConnectionInfo connection) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;

	boolean validateConnection(IZoneMinderConnectionInfo connection);

	boolean isZoneMinderUrl(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, ZoneMinderApiNotEnabledException;
	
	boolean validateLogin(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException;
	boolean isApiEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException;
	boolean isTriggerOptionEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException;
	
	void setConnected(boolean newState);
	boolean isConnected();
	
	void SubscribeMonitorEvents(IZoneMinderConnectionInfo connection, String monitorId, IZoneMinderEventSubscriber subscriber)  throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException;
	void UnsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber);

}
