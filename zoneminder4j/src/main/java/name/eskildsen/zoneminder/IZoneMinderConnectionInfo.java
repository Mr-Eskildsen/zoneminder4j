package name.eskildsen.zoneminder;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilderException;

import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;


public interface IZoneMinderConnectionInfo {

	public String getProtocolName();
	public ProtocolType getProtocolType();
	public String getHostName();
	public Integer getHttpPort();
	public Integer getTelnetPort();
	public String getUserName();
	public String getPassword();

	//@Deprecated
	//public URI getZoneMinderRootUri_() throws MalformedURLException;
	public URI getZoneMinderPortalUri() throws MalformedURLException;

	public Integer getTimeout();

	public URI buildZoneMinderPortalUri(String subPath) throws MalformedURLException;
	public URI getZoneMinderApiBaseUri() throws MalformedURLException;
	public URI buildZoneMinderApiUri(String subPath) throws MalformedURLException;


	//@Deprecated
	//String getConfigAuthenticationHashSecret();
	
	//@Deprecated
	//boolean isAuthenticationHashAllowed();
	
	boolean isAuthenticationEnabled();
	boolean isApiEnabled();

	boolean isConnected();
	
	public String printDebug();
	String getAuthHashToken() throws ZoneMinderAuthHashNotEnabled;
	
	String getPageContentAsString(URI url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException;
	
	//TODO String to URI
	//@Deprecated
	//String getPageContentAsString(String url)
	//		throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException;


	//TODO String to URI
	//@Deprecated
	//String getPageContentAsString(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException;
	
	//TODO String to URI
	//@Deprecated
	//ByteArrayOutputStream getPageContentAsByteArray(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderStreamConfigException, ZoneMinderGeneralException; 
	
	//TODO String to URI
	//@Deprecated
	//void sendPost(String url, Map<String, String> postParams) throws ZoneMinderAuthenticationException;
	
	//TODO String to URI
	//@Deprecated
	//void sendPost(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException;
	
	
	

}
