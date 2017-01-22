package name.eskildsen.zoneminder;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.ZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.internal.ZoneMinderEventManager;
import name.eskildsen.zoneminder.internal.ZoneMinderMonitorProxy;
import name.eskildsen.zoneminder.internal.ZoneMinderServerProxy;
import name.eskildsen.zoneminder.internal.ZoneMinderSession;

public class ZoneMinderFactory {

	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String basePath, String userName,
            String password, Integer timeout) throws GeneralSecurityException {
		return (IZoneMinderConnectionInfo)new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, basePath, userName, password, timeout);
	}

	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, String basePath, String userName,
            String password) throws GeneralSecurityException {
		return (IZoneMinderConnectionInfo)new ZoneMinderConnectionInfo(protocol, hostName, basePath, userName, password);
	}

	public static IZoneMinderSession CreateSession(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
		return new ZoneMinderSession(connection, true, false);
	}

	public static IZoneMinderServer getServerProxy(IZoneMinderSession session)
	{
		return new ZoneMinderServerProxy(session);
	}
	

	public static IZoneMinderMonitor getMonitorProxy(IZoneMinderSession session, String monitorId)
	{
		return new ZoneMinderMonitorProxy(session, monitorId);
	}


	

	public static boolean validateConnection(IZoneMinderConnectionInfo connection) {
		return ZoneMinderEventManager.getInstance().validateConnection(connection);
	}

	public static boolean validateLogin(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException  {
		
		return ZoneMinderEventManager.getInstance().validateLogin(connection);
	}
	

	public static void SubscribeMonitorEvents(IZoneMinderConnectionInfo connection, String monitorId, IZoneMinderEventSubscriber subscriber) throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException {
		ZoneMinderEventManager.getInstance().SubscribeMonitorEvents(connection, monitorId, subscriber);
	}


	public static void UnsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber) {
		ZoneMinderEventManager.getInstance().UnsubscribeMonitorEvents(monitorId, subscriber);
	}

}
