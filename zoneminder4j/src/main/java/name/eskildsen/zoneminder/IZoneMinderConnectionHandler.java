package name.eskildsen.zoneminder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;


import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.Fields;

import name.eskildsen.zoneminder.common.HashAuthenticationEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;


public interface IZoneMinderConnectionHandler/*extends IZoneMinderConnectionInfo*/ {

	public String getProtocolName();
	public ProtocolType getProtocolType();
	public String getHostName();
	public Integer getHttpPort();
	public Integer getTelnetPort();
	public String getUserName();
	public String getPassword();
	
	public String getStreamingUserName();
	public String getStreamingPassword();

	public URI getPortalUri() throws MalformedURLException;
	public URI getApiUri() throws MalformedURLException;
	public URI getZmsNphUri() throws MalformedURLException;

	
	public ZoneMinderContentResponse fetchContentResponse(URI uri, HttpMethod httpMethod, List<JettyQueryParameter> parameters ) throws ZoneMinderGeneralException, ZoneMinderResponseException;

	
	boolean getAuthenticationHashUseIp();
	boolean getAuthenticationHashAllowed();
	HashAuthenticationEnum getAuthenticationHashReleayMethod();
	
	String getConfigAuthenticationHashSecret();
	String getAuthHashToken() throws ZoneMinderAuthHashNotEnabled;
	
	
	boolean isAuthenticationEnabled();
	boolean isApiEnabled();
	boolean isTriggerOptionEnabled();
	

	boolean isConnected();
	boolean isAuthenticated();
	public String printDebug();
	
	
	ZoneMinderContentResponse getPageContent(URI uri) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;
	ZoneMinderContentResponse getPageContent(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;

	ZoneMinderContentResponse sendPost(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	ZoneMinderContentResponse sendPut(URI uri, Fields fields) throws MalformedURLException, ZoneMinderException;

	

}
