package name.eskildsen.zoneminder;


import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public interface IZoneMinderEventSession {
	
	boolean isConnected();

	
	void subscribeMonitorEvents( IZoneMinderConnectionHandler connection, String monitorId, IZoneMinderEventSubscriber subscriber)  throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException;
	void unsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber);

    /** *****************************************************
     * Event Methods
     * @throws IOException 
     * @throws ZoneMinderUrlNotFoundException 
     * @throws FailedLoginException 
      ***************************************************** */
	void activateForceAlarm(String monitorId, Integer priority, String reason, String note, String showText, Integer timeoutSeconds) throws IOException;
	void deactivateForceAlarm(String monitorId) throws IOException;
	


}
