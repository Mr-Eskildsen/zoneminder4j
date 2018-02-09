package name.eskildsen.zoneminder;


import java.net.MalformedURLException;

import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.event.ZoneMinderEventManager;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.internal.ZoneMinderMonitorProxy;
import name.eskildsen.zoneminder.internal.ZoneMinderServerProxy;
import name.eskildsen.zoneminder.jetty.JettyConnectionInfo;

public interface ZoneMinderFactory {

	public static String getDefaultPortalSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_PORTAL_SUBPATH;
	}
	
	public static String getDefaultApiSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_API_SUBPATH;
	}
	
	/*
	 * TODO:: UDGAAR
	 
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
*/
	
	public static IZoneMinderConnectionHandler CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String streamingUserName, String streamingPassword,  String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException {
		
		//return new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, null );
		JettyConnectionInfo jci =new JettyConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, streamingUserName, streamingPassword,  zmPortalSubPath, zmApiSubPath, timeout);
		if (jci.connect()) {
			return jci;	
		}
		return null;
		 
	}

/*	@Deprecated
	protected static IZoneMinderConnectionInfo CreateConnection_OLD(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException {
		
		//return new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, null );
		JettyConnectionInfo jci =new JettyConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout);
		if (jci.connect()) {
			return null;	
		}
		return null;
		 
	}
*/
	/*//REMOVED BECAUSE JETTY Impolementation
	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout, String loggerId) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException  {
		
		//ZoneMinderConnectionInfo connection = new ZoneMinderConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, loggerId );
		JettyConnectionInfo connection = new JettyConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout);

		//TODO:: REMOVED verifyConnectionInfo(connection);
		 
		return connection;

	}
	*/
/* TODO:: TEMPORARILY REMOVED
	public static IZoneMinderConnectionInfo CreateConnection(String protocol, String hostName, String userName, String password, String zmPortalSubPath, String zmApiSubPath, String loggerId) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException {
		
		//ZoneMinderConnectionInfo connection = new ZoneMinderConnectionInfo(protocol, hostName, userName, password, zmPortalSubPath, zmApiSubPath, loggerId);
		JettyConnectionInfo connection = new JettyConnectionInfo(protocol, hostName, userName, password, zmPortalSubPath, zmApiSubPath);
		//TODO:: REMOVED verifyConnectionInfo(connection);
		
		return connection;
	}

*/
	public static IZoneMinderEventSession CreateEventSession(IZoneMinderConnectionHandler connection)
	{

		return new ZoneMinderEventManager(connection); //ZoneMinderMonitorProxy(session, monitorId);
	}
/*	
	//TODO:: FIX Genereal EXCEPTION IN THROWS
	public static IZoneMinderHttpSession CreateHttpSession(IZoneMinderConnectionInfo connection) throws ZoneMinderApiNotEnabledException, Exception {

		return new ZoneMinderSession(connection, true, false);
	}

	@Deprecated
	public static IZoneMinderHttpSession CreateHttpSession(IZoneMinderConnectionHandler connection) throws ZoneMinderApiNotEnabledException, Exception {
		return new JettySession(connection);
	}

*/
/*	public static IZoneMinderVerification getVerification(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException
	{
		return new ZoneMinderVerification(connection);
	}
*/
/*//TODO:: REMOEVD BECAUSE OF JETTY	
	
	//TODO:: CLEANUP
	public static IZoneMinderConnectionInfo verifyConnectionInfo(ZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException {
		
		HttpSessionCore coreSession = new HttpSessionCore(connection, true);
		coreSession.updateZoneMinderConnectionInfoParams(connection);

		coreSession = new HttpSessionCore(connection, true);
		if (!coreSession.isApiEnabled()) {
			throw new ZoneMinderApiNotEnabledException();
		}
		if (coreSession.isAuthenticationEnabled()) {
			if (!coreSession.authenticate()) {
				throw new ZoneMinderAuthenticationException("Failed to authenticate");
			}
		}

		//Now we should be ready to use the more fancy session handler
		ZoneMinderSession session = new ZoneMinderSession(connection, true, true, false, false);
		Boolean isAuthenticated = session.isAuthenticated(); 
		ZoneMinderGenericProxy proxy = new ZoneMinderGenericProxy(session, true);
		//ZoneMinderServerProxy proxy = new ZoneMinderServerProxy(session);
		
		boolean authHashSettingsCorrect = false;
		boolean allowHashLogins = false;

		ZoneMinderConfig configAllowHashLogins = null;
		ZoneMinderConfig configHashIpAddress = null;
		ZoneMinderConfig configAuthRelay = null;
		ZoneMinderConfig configAuthSecret = null;
		
		//Check hostVersion
		//IZoneMinderHostVersion hostVersion  = proxy.getHostVersion();
		
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
	*/
	
	public static IZoneMinderServer getServerProxy(IZoneMinderConnectionHandler connection)
	{
		return new ZoneMinderServerProxy(connection);
	}
	

	public static IZoneMinderMonitor getMonitorProxy(IZoneMinderConnectionHandler connection, String monitorId)
	{
		return new ZoneMinderMonitorProxy(connection, monitorId);
	}
	
	

	/*
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
		return HttpSessionCore.validateConnection(connection);
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
*/
	
}
