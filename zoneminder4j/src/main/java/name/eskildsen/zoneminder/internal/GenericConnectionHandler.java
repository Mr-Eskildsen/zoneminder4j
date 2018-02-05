package name.eskildsen.zoneminder.internal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.api.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.general.Tools;
import name.eskildsen.zoneminder.jetty.HttpCore;

public abstract class GenericConnectionHandler extends HttpCore implements IZoneMinderConnectionHandler {

	// Default values for Monitor parameters
	private static final Integer DEFAULT_HTTP_PORT = 80;
	private static final Integer DEFAULT_HTTPS_PORT = 443;
	private static final Integer DEFAULT_TELNET_PORT = 6802;

	private static final Integer DEFAULT_TIMEOUT = 2000;

	private ProtocolType protocol;
	private String hostName;
	private Integer portHttp;
	private Integer portTelnet;
	private String _userName;
	private String _password;
	private Integer timeout = DEFAULT_TIMEOUT;
	private String zoneMinderPortalPath = "";
	private String zoneMinderApiPath = "";
	private String zoneMinderZmsPath = "";

	
//	private URL zoneMinderUrl = null;
	
	
	private boolean _connected = false;



	/*
	 * 
	 * CONFIGURATION SETTINGS
	 */
	private boolean isApiEnabled = false;
	private boolean triggerOptiionEnabled = false;
	
	
	//Authentication Hash
	private boolean useAuthentication = false;
	private boolean allowHashSecrets = false;
	private String  authenticationHashSecret = null;
	private String authticationHashRelayMethod = "";
	private boolean authticationHashUseIps = false;
    
	private Date authHashExpires = null;
	private String authGeneratedHash = "";
	
	
	protected  abstract boolean authenticate() throws ZoneMinderGeneralException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, MalformedURLException;
	protected void setConnected(boolean connected) {
		_connected = connected;
	}
		
	protected void setUserName(String username) {
		if (username == null) {
			_userName = "";
		} else {
			_userName = username;
		}
	}

	protected void setPassword(String password) {
		if (password == null) {
			_password = "";
		} else {
			_password = password;
		}
	}


	//protected abstract String getCgiBinPath();

	public GenericConnectionHandler(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
         String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout ) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException {

		Initialize(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout);
	}

	public GenericConnectionHandler(String protocol, String hostName, String userName, String password, String zmPortalSubPath, String zmApiSubPath) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException /*throws GeneralSecurityException */{
		int port = DEFAULT_HTTP_PORT;
 	
 		if (protocol.equalsIgnoreCase("https"))
	 	{
	 		port = DEFAULT_HTTPS_PORT;
	 	}

		Initialize(protocol, hostName, port, DEFAULT_TELNET_PORT, userName, password, zmPortalSubPath, zmApiSubPath, DEFAULT_TIMEOUT);
		
	}

	protected void finalize() throws Throwable
	{
		disconnect();
	}

	public void disconnect() throws Throwable  {
		onDisconnect();
	}
	
	
	public boolean connect() throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException {
		onConnect();
		return isConnected();
	}

	
	protected abstract void onConnect() throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException;
	protected abstract void onDisconnect() throws Throwable;
	
	protected void Initialize(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
			String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException {
		if ((portHttp == null) || (portHttp == 0)) {
			if (protocol.equalsIgnoreCase("https")) {
				portHttp = DEFAULT_HTTPS_PORT;
			} else {
				portHttp = DEFAULT_HTTP_PORT;
			}
		}

		if ((portTelnet == null) || (portTelnet == 0)) {
			portTelnet = DEFAULT_TELNET_PORT;
		}

		this.protocol = ProtocolType.getEnum(protocol);
		this.hostName = hostName;
		this.portHttp = portHttp;
		this.portTelnet = portTelnet;
		this.zoneMinderPortalPath = Tools.fixPath(zmPortalSubPath);
		this.zoneMinderApiPath = Tools.fixPath(zmApiSubPath);

		setUserName(userName);
		setPassword(password);
		this.timeout = timeout;
		// TODO:: KILL LOGGER		
		//this.loggerId = loggerId;
		// TODO:: KILL LOGGER
		// this.logLevel = LogLevel.NONE;

		
	}

	
	
	
	
	
	
	
	
	
	
	
	public String getProtocolName() {
		return protocol.name();
	}

	public ProtocolType getProtocolType() {
		return protocol;
	}

	public String getHostName() {
		return hostName;
	}

	public Integer getHttpPort() {
		return portHttp;
	}

	public Integer getTelnetPort() {
		return portTelnet;
	}

	public String getUserName() {
		return _userName;
	}

	public String getPassword() {
		return _password;
	}

	public String getZoneMinderPortalPath() {
		return zoneMinderPortalPath;
	}

	public String getZoneMinderApiPath() {
		return zoneMinderApiPath;
	}
	
	
	public String getZoneMinderStreamingPath() {
		return zoneMinderZmsPath;
	}
	
	public Integer getTimeout() {
		return timeout;
	}


	/***
	 * 
	 * HTTP Basic operations
	 * @throws ZoneMinderAuthenticationException 
	 * @throws MalformedURLException 
	 * @throws ZoneMinderGeneralException 
	 * @throws UriBuilderException 
	 * @throws IllegalArgumentException 
	 * @throws ZoneMinderResponseException 
	 * 
	 */
	//public abstract String getPageContent(String url) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException;
	//public abstract void sendPost(String url, Map<String, String> postParams) throws ZoneMinderAuthenticationException;
	
	

	protected void setApiEnabled(boolean enabled) {isApiEnabled = enabled;	}
	protected void setAuthenticationEnabled(boolean enabled) {useAuthentication = enabled;}
	protected void setTriggerOptionEnabled(boolean enabled) {triggerOptiionEnabled = enabled;}
	protected void setZoneMinderStreamingPath(String pathZms) {zoneMinderZmsPath = pathZms;}
	
	protected void setAuthenticationHashAllowed(boolean allowHashSecrets) {this.allowHashSecrets = allowHashSecrets;}
	protected void setAuthenticationHashSecret(String authHashSecret) {this.authenticationHashSecret = authHashSecret;}
	protected void setAuthenticationHashReleayMethod(String authRelayMethod) {this.authticationHashRelayMethod = authRelayMethod;}
	protected void setAuthenticationHashUseIp(boolean useIp) {this.authticationHashUseIps = useIp;}
	
	@Override
	public String getConfigAuthenticationHashSecret() {
		return this.authenticationHashSecret;
	}

	@Override
	public boolean isAuthenticationHashAllowed() {
		return (authticationHashRelayMethod.equalsIgnoreCase("none") && allowHashSecrets);
	}
	
	@Override
	public boolean isAuthenticationEnabled() {
		return useAuthentication;
	}

	@Override
	public boolean isApiEnabled() {
		return isApiEnabled;
	}

	@Override
	public boolean isTriggerOptionEnabled() {
		return triggerOptiionEnabled;
	}

	@Override
	public URI getPortalUri() throws MalformedURLException {
		return UriBuilder.fromUri(
				(new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderPortalPath())).toString())
				.build();
	}
	@Override
	public URI getApiUri() throws MalformedURLException {
		return UriBuilder
				.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderApiPath())).toString())
				.build();
	}
	
	@Override
	public URI getZmsNphUri() throws MalformedURLException {
		return UriBuilder.fromUri(
				(new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderStreamingPath())).toString())
				.build();
	}
	

	
	protected URI buildZoneMinderUri(URI root, String subPath) throws MalformedURLException {

		return UriBuilder.fromUri(root).path(subPath).build();
	}

	
	/*
	
	public URI getZoneMinderPortalUri() throws MalformedURLException {

		return UriBuilder.fromUri(
				(new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderPortalPath())).toString())
				.build();
	}
*/

	//TODO - Skal være der - renames
	//@Override
	public URI getZoneMinderRootUri_() throws MalformedURLException {
		return UriBuilder.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), "")).toString()).build();
	}

	

	
	/*
	//TODO HERTIL

	//TODO WHY IS IT Interface?
	
	public URI buildZoneMinderPortalUri(String subPath) throws MalformedURLException {

		return buildZoneMinderUri(getZoneMinderPortalUri(), subPath);
	}

	//TODO WHY IS IT Interface?
	public URI buildZoneMinderApiUri(String subPath) throws MalformedURLException {

		return buildZoneMinderUri(getZoneMinderApiBaseUri(), subPath);
	}
*/
	public String printDebug() {
		return String.format(
				"Protocol='%s', Type='%s', Host='%s', PortalPort='%d', TelnetPort='%d', User='%s', Password='%s', PortalPath='%s', ApiPath='%s', Timeoutr='%d'",
				getProtocolName(), getProtocolType(), getHostName(), getHttpPort(), getTelnetPort(), getUserName(),
				getPassword(), getZoneMinderPortalPath(), getZoneMinderApiPath(), getTimeout());
	}


	@Override
	public boolean isConnected() {
		return _connected;
	}
	
	@Override
	public String getAuthHashToken() throws ZoneMinderAuthHashNotEnabled {

		
		if (isAuthenticationHashAllowed()==true) {

			Date now = new Date();
			
			if ((authGeneratedHash == "") || (!authHashExpires.after(now))) {
				authGeneratedHash = generateAuthenticationHash();
			}
			return authGeneratedHash;
		}
		
		throw new ZoneMinderAuthHashNotEnabled();
	}
	
	protected String generateAuthenticationHash() throws ZoneMinderAuthHashNotEnabled
	{
		try {
	
			//TODO Also check for IP adresses!
			Calendar calendar = Calendar.getInstance();
						
			String input = String.format("%s.%s.%s.%d.%d.%d.%d", getConfigAuthenticationHashSecret(), getUserName(), getPassword(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(input.getBytes());
			StringBuffer generatedHash = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				generatedHash.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			calendar.setTime(new Date()); // sets calendar time/date

			calendar.add(Calendar.MINUTE, 60); // adds 60 minutes, to force recalculation after one hour
			authHashExpires = calendar.getTime();
		    
		    return generatedHash.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			
		}
		return null;	
	}


	
	
	
	
	
}
