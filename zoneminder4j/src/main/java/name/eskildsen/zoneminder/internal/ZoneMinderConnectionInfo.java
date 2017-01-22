package name.eskildsen.zoneminder.internal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
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
    private String zoneMinderBasePath = "";
    private URL zoneMinderUrl = null;
   
   // private Object syncObject = new Object();

    public ZoneMinderConnectionInfo(String protocol, String hostName, Integer portHttp, Integer portTelnet, String basePath, String userName,
            String password, Integer timeout ) {

    	Initialize(protocol, hostName, portHttp, portTelnet, basePath, userName, password, timeout);
    }

    
    public ZoneMinderConnectionInfo(String protocol, String hostName, String basePath, String userName, String password) throws GeneralSecurityException {
    	int port = DEFAULT_HTTP_PORT;
    	
    	if (protocol.equalsIgnoreCase("https"))
    	{
    		port = DEFAULT_HTTPS_PORT;
    	}
    	
		Initialize(protocol, hostName, port, DEFAULT_TELNET_PORT, basePath, userName, password, DEFAULT_TIMEOUT);
		
    }
    
    protected void Initialize(String protocol, String hostName, Integer portHttp, Integer portTelnet, String basePath, String userName, String password, Integer timeout)
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
        this.zoneMinderBasePath = Tools.fixPath(basePath);
        this.userName = userName;
        this.password = password;
        this.timeout = timeout;
		
    }

    

    //public URI getServerRootURI() {return uriZmServerRoot;}
    public String getProtocolName() {return protocol.name();}
    public ProtocolType getProtocolType() {return protocol;}
    public String getHostName() {return hostName;}
    public Integer getHttpPort() {return portHttp;}
    public Integer getTelnetPort() {return portTelnet;}
    public String getUserName() { return userName;}
    public String getPassword() {return password;}
    public String getZoneMinderBasePath()  {return zoneMinderBasePath ;}
    public Integer getTimeout() {return timeout;}
    
    public URI getZoneMinderRootUri() throws MalformedURLException {
		
    	return UriBuilder.fromUri((new URL(getProtocolName(), getHostName(), getHttpPort(), getZoneMinderBasePath())).toString()).build();
    }
   
}
