package name.eskildsen.zoneminder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.ZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.internal.ZoneMinderEventManager;
import name.eskildsen.zoneminder.internal.ZoneMinderMonitorProxy;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.internal.ZoneMinderServerProxy;
import name.eskildsen.zoneminder.internal.ZoneMinderSession;
import name.eskildsen.zoneminder.logging.LogEntry;
import name.eskildsen.zoneminder.logging.LogFactory;
import name.eskildsen.zoneminder.logging.LogLevel;
import name.eskildsen.zoneminder.logging.LogManager;

public class ZoneMinderFactory {

	public static String createLogger() {
		return LogFactory.createLogger();
	}
	
	public static String getDefaultPortalSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_PORTAL_SUBPATH;
	}
	
	public static String getDefaultApiSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_API_SUBPATH;
	}
	
	
	public static List<ILogEntry> getLogEntries(String loggerId) {
	
		LogManager logger = LogFactory.getLogger(loggerId);
		return logger.getLogEntries();
	}
	
	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout, String loggerId) throws GeneralSecurityException {
		
		return (IZoneMinderConnectionInfo)new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, loggerId );

	}

	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, String userName, String password, String zmPortalSubPath, String zmApiSubPath, String loggerId) throws GeneralSecurityException {
		return (IZoneMinderConnectionInfo)new ZoneMinderConnectionInfo(protocol, hostName, userName, password, zmPortalSubPath, zmApiSubPath, loggerId);
	}

	public static IZoneMinderSession CreateSession(IZoneMinderConnectionInfo connection) throws ZoneMinderApiNotEnabledException, Exception {
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


	
	public static boolean isZoneMinderUrl(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, ZoneMinderApiNotEnabledException {
		
		return ZoneMinderEventManager.getInstance().isZoneMinderUrl(connection);

	}

	public static boolean isApiEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException  {
		
		return ZoneMinderEventManager.getInstance().isApiEnabled(connection);

	}
	
	public static boolean isTriggerOptionEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException {
		
		return ZoneMinderEventManager.getInstance().isTriggerOptionEnabled(connection);

	}
	
	
	public static boolean validateConnection(IZoneMinderConnectionInfo connection) {
		return ZoneMinderEventManager.getInstance().validateConnection(connection);
	}

	
	public static boolean validateLogin(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException  {
		
		return ZoneMinderEventManager.getInstance().validateLogin(connection);
	}
	

	public static void SubscribeMonitorEvents(IZoneMinderConnectionInfo connection, String monitorId, IZoneMinderEventSubscriber subscriber) throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException {
		ZoneMinderEventManager.getInstance().SubscribeMonitorEvents(connection, monitorId, subscriber);
	}


	public static void UnsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber) {
		ZoneMinderEventManager.getInstance().UnsubscribeMonitorEvents(monitorId, subscriber);
	}

}
