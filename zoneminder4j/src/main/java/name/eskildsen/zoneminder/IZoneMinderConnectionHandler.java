package name.eskildsen.zoneminder;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilderException;

import org.eclipse.jetty.http.HttpMethod;

import name.eskildsen.zoneminder.api.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
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

	public URI getPortalUri() throws MalformedURLException;
	public URI getApiUri() throws MalformedURLException;
	public URI getZmsNphUri() throws MalformedURLException;

	
	public ZoneMinderContentResponse fetchContentResponse(URI uri, HttpMethod httpMethod, List<JettyQueryParameter> parameters ) throws ZoneMinderGeneralException, ZoneMinderResponseException;
		
	
	//public Integer getTimeout();

/*	public URI buildZoneMinderPortalUri(String subPath) throws MalformedURLException;
	public URI getZoneMinderApiBaseUri() throws MalformedURLException;
	public URI getZoneMinderCgiBinBaseUri() throws MalformedURLException;
	public URI buildZoneMinderCgiBinUri(String subPath) throws MalformedURLException;
*/

	
	boolean isAuthenticationHashAllowed();
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
	ZoneMinderContentResponse sendPut(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;

	/*
	String getPageContentAsString(URI uri) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;
	String getPageContentAsString(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;
	
	ByteArrayOutputStream getPageContentAsByteArray(URI url) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;
	ByteArrayOutputStream getPageContentAsByteArray(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderStreamConfigException;
*/	
	
	

}
