package name.eskildsen.zoneminder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.api.exception.ZoneMinderCredentialsMissingException;
import name.eskildsen.zoneminder.exception.ZoneMinderNotLoggedInException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.HttpSessionCore;
import name.eskildsen.zoneminder.internal.ZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.internal.ZoneMinderEventManager;
import name.eskildsen.zoneminder.internal.ZoneMinderGenericProxy;
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
		List<ILogEntry> result = null;
		try {
			ILogManager logger = LogFactory.getLogger(loggerId);
			if (logger!=null) {
				result =logger.getLogEntries(); 
			}
		}
		catch(Exception e) {
			result = new ArrayList<ILogEntry>();
		}
		
		return result;
	}

	
	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderNotLoggedInException, ZoneMinderCredentialsMissingException {
		
		return updateConnectionInfo(new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, null ));

	}

	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout, String loggerId) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderNotLoggedInException, ZoneMinderCredentialsMissingException {
		
		return updateConnectionInfo(new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, loggerId ));

	}

	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, String userName, String password, String zmPortalSubPath, String zmApiSubPath, String loggerId) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderNotLoggedInException, ZoneMinderCredentialsMissingException {
		return updateConnectionInfo(new ZoneMinderConnectionInfo(protocol, hostName, userName, password, zmPortalSubPath, zmApiSubPath, loggerId));
	}

	public static IZoneMinderSession CreateSession(IZoneMinderConnectionInfo connection, boolean connectHttp, boolean connectTelnet) throws ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException, Exception {
		return new ZoneMinderSession(connection, connectHttp, connectTelnet);
	}

	
	protected static IZoneMinderConnectionInfo updateConnectionInfo(ZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderNotLoggedInException, ZoneMinderCredentialsMissingException {
		
		HttpSessionCore coreSession = new HttpSessionCore(connection);
		coreSession.updateZoneMinderConnectionInfoParams(connection);
		if (!coreSession.isApiEnabled()) {
			throw new ZoneMinderApiNotEnabledException();
		}
		if (coreSession.isAuthenticationEnabled()) {
			if (!coreSession.authenticate()) {
				throw new ZoneMinderNotLoggedInException("");
			}
		}

		//Now we should be ready to use the more fancy session handler
		ZoneMinderSession session = new ZoneMinderSession(connection, true, true, false, false);
		ZoneMinderGenericProxy proxy = new ZoneMinderGenericProxy(session, true);
		
		boolean authHashSettingsCorrect = false;
		boolean allowHashLogins = false;

		ZoneMinderConfig configAllowHashLogins = null;
		ZoneMinderConfig configHashIpAddress = null;
		ZoneMinderConfig configAuthRelay = null;
		ZoneMinderConfig configAuthSecret = null;
		
		
		//Check if authentication is enabled
		configAllowHashLogins = proxy.getConfig(ZoneMinderConfigEnum.ZM_AUTH_HASH_LOGINS); //	Allow login by authentication hash (?)
		
		//Check if it is set to none
		//Check if External Ip is included (must be false)
		configHashIpAddress = proxy.getConfig(ZoneMinderConfigEnum.ZM_AUTH_HASH_IPS); //	Include IP addresses in the authentication hash (?)	
		Integer i1 = proxy.getHttpResponseCode();
		
		configAuthRelay = proxy.getConfig(ZoneMinderConfigEnum.ZM_AUTH_RELAY); //	Method used to relay authentication information (?)	 hashed   plain   none
		Integer i2 = proxy.getHttpResponseCode();
		
		//Get AuthHashSecret
		configAuthSecret = proxy.getConfig(ZoneMinderConfigEnum.ZM_AUTH_HASH_SECRET); //	Secret for encoding hashed authentication information (?)	
		Integer i3 = proxy.getHttpResponseCode();
		try {
			authHashSettingsCorrect = ((configAuthRelay.getValueAsString().equalsIgnoreCase("none")) && (configHashIpAddress.getValueAsString().equals("0"))) ? true : false;
			allowHashLogins = ((configAllowHashLogins.getValueAsString().equals("1")) && (authHashSettingsCorrect==true)) ? true : false;
		}
		catch(Exception ex) {
			authHashSettingsCorrect = false;
			allowHashLogins = false;
			
		}
		
		connection.setConfigAuthenticationHashAllowed(allowHashLogins);
		if (allowHashLogins) {
			connection.setConfigAuthenticationHashSecret(configAuthSecret.getValueAsString());
		}
		
		
		ZoneMinderConfig configOptTriggers = proxy.getConfig(ZoneMinderConfigEnum.ZM_OPT_TRIGGERS);
		boolean b = configOptTriggers.getvalueAsBoolean();
	
		return connection;
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
