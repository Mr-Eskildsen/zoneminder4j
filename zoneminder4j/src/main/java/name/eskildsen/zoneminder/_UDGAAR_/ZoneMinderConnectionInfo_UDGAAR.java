package name.eskildsen.zoneminder.internal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import name.eskildsen.zoneminder.ILogManager;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.ZoneMinderFactory;
import name.eskildsen.zoneminder.logging.LogFactory;
import name.eskildsen.zoneminder.logging.LogLevel;
import name.eskildsen.zoneminder.general.ProtocolType;

public class ZoneMinderConnectionInfo implements IZoneMinderConnectionInfo {
	   
    
    // Default values for Monitor parameters
	private static final Integer DEFAULT_HTTP_PORT = 80;
    private static final Integer DEFAULT_HTTPS_PORT = 443;
    private static final Integer DEFAULT_TELNET_PORT = 6802;

    private static final Integer DEFAULT_TIMEOUT = 2000;
    //private Boolean connected = false;

    //private URI uriZmServerRoot = null;
    private ProtocolType protocol;
    private String hostName;
    private Integer portHttp;
    private Integer portTelnet;
    private String userName;
    private String password;
    private Integer timeout = DEFAULT_TIMEOUT;
    private String zoneMinderPortalPath = "";
    private String zoneMinderApiPath = "";
    private String loggerId = "";
    private  LogLevel logLevel;
    private URL zoneMinderUrl = null;
   
   // private Object syncObject = new Object();

    public ZoneMinderConnectionInfo(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout, String loggerId ) {

    	Initialize(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout, loggerId);
    }

    
    public ZoneMinderConnectionInfo(String protocol, String hostName, String userName, String password, String zmPortalSubPath, String zmApiSubPath, String loggerId) throws GeneralSecurityException {
    	int port = DEFAULT_HTTP_PORT;
    	
    	if (protocol.equalsIgnoreCase("https"))
    	{
    		port = DEFAULT_HTTPS_PORT;
    	}

		Initialize(protocol, hostName, port, DEFAULT_TELNET_PORT, userName, password, zmPortalSubPath, zmApiSubPath, DEFAULT_TIMEOUT, loggerId);
		
    }
    
    protected void Initialize(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName, String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout, String loggerId)
    {
    	if ((portHttp==null) || (portHttp==0))  
    	{
    		if (protocol.equalsIgnoreCase("https"))
        	{
        		portHttp = DEFAULT_HTTPS_PORT;
        	}
    		else {
    			portHttp = DEFAULT_HTTP_PORT;
    		}
    	}	
    	
    	
    	if ((portTelnet==null) || (portTelnet==0))  
    	{
    		portTelnet = DEFAULT_TELNET_PORT;
    	}		
    		
        this.protocol = ProtocolType.getEnum(protocol);
        this.hostName = hostName;
        this.portHttp = portHttp;
        this.portTelnet = portTelnet;
        this.zoneMinderPortalPath = Tools.fixPath(zmPortalSubPath);
        this.zoneMinderApiPath = Tools.fixPath(zmApiSubPath);
        this.userName = userName;
        this.password = password;
        this.timeout = timeout;
        this.loggerId = loggerId;
        this.logLevel = LogLevel.NONE;
		
    }

    

    //public URI getServerRootURI() {return uriZmServerRoot;}
    public String getProtocolName() {return protocol.name();}
    public ProtocolType getProtocolType() {return protocol;}
    public String getHostName() {return hostName;}
    public Integer getHttpPort() {return portHttp;}
    public Integer getTelnetPort() {return portTelnet;}
    public String getUserName() { return userName;}
    public String getPassword() {return password;}
    public String getZoneMinderPortalPath()  {return zoneMinderPortalPath ;}
    public String getZoneMinderApiPath()  {return zoneMinderApiPath;}
    public Integer getTimeout() {return timeout;}
    public String getLoggerId() {return loggerId;}
    

    
    
    private boolean bUseAuthentication = false;
	private boolean bApiEnabled = false;
	private boolean allowHashSecrets = false;
	private String authenticationHashSecret = null;
	
	public void setAuthenticationEnabled(boolean enabled) {bUseAuthentication = enabled;}
	public void setApiEnabled(boolean enabled) {bApiEnabled = enabled;}

	//TODO: make thread safe
	public void setConfigAuthenticationHashAllowed(boolean allowHashSecrets) {this.allowHashSecrets = allowHashSecrets;}
	//TODO: make thread safe
	public void setConfigAuthenticationHashSecret(String authenticationHashSecret) {this.authenticationHashSecret = authenticationHashSecret;}
	
	//TODO: make thread safe
	public String getConfigAuthenticationHashSecret() {return this.authenticationHashSecret;}
	
	public boolean isAuthenticationHashAllowed() { return this.allowHashSecrets;}

	public boolean isAuthenticationEnabled() {return bUseAuthentication;}
	public boolean isApiEnabled() {return bApiEnabled;}

    public void setLogLevel(LogLevel level) 
    {
    	
    	this.logLevel = level;
    	ILogManager logger = LogFactory.getLogger(getLoggerId());
    	if (logger!=null) {
    		logger.setLogLevel(level);
    	}
	}
    
    
    public URI getZoneMinderApiBaseUri() throws MalformedURLException {
    	
    	return UriBuilder.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderApiPath() )).toString()).build();
    }
    
    		
    public URI getZoneMinderPortalUri() throws MalformedURLException {
		
    	return UriBuilder.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderPortalPath())).toString()).build();
    }


    public URI getZoneMinderRootUri_() throws MalformedURLException {
		
    	return UriBuilder.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), "" )).toString()).build();
    }

    
	protected URI buildZoneMinderUri(URI root, String subPath) throws MalformedURLException {
		
		return UriBuilder.fromUri(root).path(subPath).build();
	}

	

	@Override
	public URI buildZoneMinderPortalUri(String subPath) throws MalformedURLException {
		
		
		return buildZoneMinderUri(getZoneMinderPortalUri(), subPath);
	}

	
	@Override
	public URI buildZoneMinderApiUri(String subPath) throws MalformedURLException {
		
		return buildZoneMinderUri(getZoneMinderApiBaseUri(), subPath);
	}

	public String printDebug()
	{
		return String.format("Protocol='%s', Type='%s', Host='%s', PortalPort='%d', TelnetPort='%d', User='%s', Password='%s', PortalPath='%s', ApiPath='%s', Timeoutr='%d'", getProtocolName(), getProtocolType(), getHostName(), getHttpPort(), getTelnetPort(),getUserName(), getPassword(), getZoneMinderPortalPath(), getZoneMinderApiPath(), getTimeout());
	}



}
