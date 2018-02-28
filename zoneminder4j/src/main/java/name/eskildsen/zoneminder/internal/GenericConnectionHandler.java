package name.eskildsen.zoneminder.internal;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.common.HashAuthenticationEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
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
	private String _streamingUserName;
	private String _streamingPassword;
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
	private boolean triggerOptionEnabled = false;
	
	
	//Authentication Hash
	private boolean useAuthentication = false;
	private boolean allowHashSecrets = false;
	private String  authenticationHashSecret = null;
	private String authticationHashRelayMethod_ = "";
	private HashAuthenticationEnum authticationHashRelayMethod;
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

	protected void setStreamingUserName(String username) {
		_streamingUserName = username;
	}

	protected void setStreamingPassword(String password) {
		_streamingPassword = password;
	}


	//protected abstract String getCgiBinPath();

	public GenericConnectionHandler(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
         String password, String streamingUserName, String streamingPassword, String zmPortalSubPath, String zmApiSubPath, Integer timeout ) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException {

		Initialize(protocol, hostName, portHttp, portTelnet, userName, password, streamingUserName, streamingPassword, zmPortalSubPath, zmApiSubPath, timeout);
	}

	public GenericConnectionHandler(String protocol, String hostName, String userName, String password, String streamingUserName, String streamingPassword, String zmPortalSubPath, String zmApiSubPath) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException /*throws GeneralSecurityException */{
		int port = DEFAULT_HTTP_PORT;
 	
 		if (protocol.equalsIgnoreCase("https"))
	 	{
	 		port = DEFAULT_HTTPS_PORT;
	 	}

		Initialize(protocol, hostName, port, DEFAULT_TELNET_PORT, userName, password, streamingUserName, streamingPassword, zmPortalSubPath, zmApiSubPath, DEFAULT_TIMEOUT);
		
	}

	protected void finalize() throws Throwable
	{
		disconnect();
	}

	public void disconnect() throws Throwable  {
		onDisconnect();
	}
	
	
	public boolean connect() throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderInvalidData, ZoneMinderResponseException {
		onConnect();
		return isConnected();
	}

	
	protected abstract void onConnect() throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderInvalidData, ZoneMinderResponseException;
	protected abstract void onDisconnect() throws Throwable;
	
	protected void Initialize(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
			String password, String streamingUserName, String streamingPassword, String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException {
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
		setStreamingUserName(streamingUserName);
		setStreamingPassword(streamingPassword);
		this.timeout = timeout;
	}

	
	
	
	
	
	
	
	
	
	
	@Override
	public String getProtocolName() {
		return protocol.name();
	}

	@Override
	public ProtocolType getProtocolType() {
		return protocol;
	}
	
	@Override
	public String getHostName() {
		return hostName;
	}

	@Override
	public Integer getHttpPort() {
		return portHttp;
	}

	@Override
	public Integer getTelnetPort() {
		return portTelnet;
	}

	@Override
	public String getUserName() {
		return _userName;
	}

	@Override
	public String getPassword() {
		return _password;
	}


	@Override
	public String getStreamingUserName() {
		if (_streamingUserName == null) {
			return getUserName();
		}
		return _streamingUserName;
	}


	@Override
	public String getStreamingPassword() {
		
		if (_streamingPassword == null) {
			return getPassword();
		}
		return _streamingPassword;
	}
	
	@Override
	public boolean getAuthenticationHashAllowed() 						{return allowHashSecrets;}
	@Override
	public HashAuthenticationEnum getAuthenticationHashReleayMethod()	{return authticationHashRelayMethod;}

	@Override
	public boolean getAuthenticationHashUseIp() {
		return authticationHashUseIps;
	}
	
	@Override
	public String getConfigAuthenticationHashSecret() {
		return this.authenticationHashSecret;
	}

	/*@Override
	public boolean isAuthenticationHashAllowed() {
		return (getAuthenticationHashReleayMethod()==HashAuthenticationEnum.Hashed && getAuthenticationHashAllowed());
	}
	*/
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
		return triggerOptionEnabled;
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
	

	
	
	protected Integer getTimeout() {return timeout;}


	protected String getZoneMinderPortalPath() {return zoneMinderPortalPath;}
	protected String getZoneMinderApiPath() {return zoneMinderApiPath;}
	protected String getZoneMinderStreamingPath() {return zoneMinderZmsPath;}


	
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
	protected void setTriggerOptionEnabled(boolean enabled) {triggerOptionEnabled = enabled;}
	protected void setZoneMinderStreamingPath(String pathZms) {zoneMinderZmsPath = pathZms;}
	
	protected void setAuthenticationHashAllowed(boolean allowHashSecrets) {this.allowHashSecrets = allowHashSecrets;}
	protected void setAuthenticationHashSecret(String authHashSecret) {this.authenticationHashSecret = authHashSecret;}
	protected void setAuthenticationHashReleayMethod(String authRelayMethod) {this.authticationHashRelayMethod = HashAuthenticationEnum.getEnum(authRelayMethod);}
	protected void setAuthenticationHashUseIp(boolean useIp) {this.authticationHashUseIps = useIp;}
	

	
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

		

		//If we used hashed pasword and Relay method is Hashed -> Maintain hashed password
		if (getAuthenticationHashAllowed() && getAuthenticationHashReleayMethod()==HashAuthenticationEnum.Hashed) {

			Date now = new Date();
			if ((authGeneratedHash == "") || (!authHashExpires.after(now))) {
				authGeneratedHash = generateAuthenticationHash();
			}
			return authGeneratedHash;
		}
		
		throw new ZoneMinderAuthHashNotEnabled();
	}
	
	private String generateAuthenticationHash() throws ZoneMinderAuthHashNotEnabled
	{
		// $authKey = ZM_AUTH_HASH_SECRET.$user['Username'].$user['Password'].$remoteAddr.$time[2].$time[3].$time[4].$time[5];
		
		try {
			//https://github.com/ZoneMinder/ZoneMinder/blob/master/web/includes/functions.php#L129
			String seed = "";
			//TODO Also check for IP adresses!
			Calendar calendar = Calendar.getInstance();
			
			if (getAuthenticationHashUseIp()) {
//				seed = String.format("%s.%s.%s.%s.%d.%d.%d.%d", getConfigAuthenticationHashSecret(), getUserName(), getPassword(), getLocalHostLANAddress().getHostAddress(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
				seed = String.format("%s%s%s%s%d%d%d%d", getConfigAuthenticationHashSecret(), getStreamingUserName(), getStreamingPassword(), getLocalHostLANAddress().getHostAddress(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)-1900);
			}
			else {
				seed = String.format("%s%s%s%d%d%d%d", getConfigAuthenticationHashSecret(), getStreamingUserName(), getStreamingPassword(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)-1900);
			}
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(seed.getBytes());
			StringBuffer generatedHash = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				generatedHash.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			calendar.setTime(new Date()); // sets calendar time/date

			calendar.add(Calendar.MINUTE, 5); // adds 60 minutes, to force recalculation after one hour
			authHashExpires = calendar.getTime();
		    
		    return generatedHash.toString();
		} catch (java.security.NoSuchAlgorithmException|UnknownHostException e) {
			//TODO Handle Exceptions
		}
		return null;	
	}

	/**
	 * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
	 * <p/>
	 * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
	 * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
	 * way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
	 * specify the algorithm used to select the address returned under such circumstances, and will often return the
	 * loopback address, which is not valid for network communication. Details
	 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
	 * <p/>
	 * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
	 * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
	 * a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
	 * first site-local address if the machine has more than one), but if the machine does not hold a site-local
	 * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
	 * <p/>
	 * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
	 * calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
	 * <p/>
	 *
	 * @throws UnknownHostException If the LAN address of the machine cannot be found.
	 */
	private InetAddress getLocalHostLANAddress() throws UnknownHostException {
	    try {
	        InetAddress candidateAddress = null;
	        // Iterate all NICs (network interface cards)...
	        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // Iterate all IP addresses assigned to each card...
	            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {

	                    if (inetAddr.isSiteLocalAddress()) {
	                        // Found non-loopback site-local address. Return it immediately...
	                        return inetAddr;
	                    }
	                    else if (candidateAddress == null) {
	                        // Found non-loopback address, but not necessarily site-local.
	                        // Store it as a candidate to be returned if site-local address is not subsequently found...
	                        candidateAddress = inetAddr;
	                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
	                        // only the first. For subsequent iterations, candidate will be non-null.
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            // We did not find a site-local address, but we found some other non-loopback address.
	            // Server might have a non-site-local address assigned to its NIC (or it might be running
	            // IPv6 which deprecates the "site-local" concept).
	            // Return this non-loopback candidate address...
	            return candidateAddress;
	        }
	        // At this point, we did not find a non-loopback address.
	        // Fall back to returning whatever InetAddress.getLocalHost() returns...
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        if (jdkSuppliedAddress == null) {
	            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
	        }
	        return jdkSuppliedAddress;
	    }
	    catch (Exception e) {
	        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
	        unknownHostException.initCause(e);
	        throw unknownHostException;
	    }
	}
	
	
	
	
	
}
