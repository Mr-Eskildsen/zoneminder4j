package name.eskildsen.zoneminder;

import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.logging.LogLevel;

public interface IZoneMinderConnectionInfo {
	public String getProtocolName();

	public ProtocolType getProtocolType();

	public String getHostName();

	public Integer getHttpPort();

	public Integer getTelnetPort();

	public String getUserName();

	public String getPassword();

	public String getLoggerId();

	public URI getZoneMinderRootUri_() throws MalformedURLException;
	public URI getZoneMinderPortalUri() throws MalformedURLException;

	public Integer getTimeout();

	public URI buildZoneMinderPortalUri(String subPath) throws MalformedURLException;
	
	public URI getZoneMinderApiBaseUri() throws MalformedURLException;
	public URI buildZoneMinderApiUri(String subPath) throws MalformedURLException;


	String getConfigAuthenticationHashSecret();
	boolean isAuthenticationHashAllowed();

	boolean isAuthenticationEnabled();
	boolean isApiEnabled();

	
	public String printDebug();
	
	public void setLogLevel(LogLevel level);
}
