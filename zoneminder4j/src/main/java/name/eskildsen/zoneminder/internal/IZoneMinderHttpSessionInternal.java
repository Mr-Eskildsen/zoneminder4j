package name.eskildsen.zoneminder.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.UriBuilderException;

import name.eskildsen.zoneminder.IZoneMinderHttpSession_NA;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;

public interface IZoneMinderHttpSessionInternal extends IZoneMinderHttpSession_NA {
	
	//TODO REDIRECTS TO ConnectionInfo
	public String getPageContent(String url) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException;
	
	//TODO REDIRECTS TO ConnectionInfo
	public void sendPost(String url, Map<String, String> postParams) throws ZoneMinderAuthenticationException;
	//public ByteArrayOutputStream getAsByteArray(String url) throws MalformedURLException, IOException;
	
	//TODO Deprecated
	@Deprecated //"Use sendPost instead")
	public String sendRequest(URI uri, ZoneMinderHttpRequest Request, String postParams) throws MalformedURLException, IOException;
	
	//TODO Deprecated
	@Deprecated 
	public String getDocumentAsStringExt(URI uri, Boolean verifyConnection, String postParams) throws MalformedURLException, IOException, ZoneMinderAuthenticationException;
	//TODO Deprecated
	@Deprecated 
	public String getDocumentAsString(URI uri, Boolean verifyConnection) throws MalformedURLException, IOException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException;
	
	//TODO Deprecated
	@Deprecated 
	public ByteArrayOutputStream getAsByteArray(URI uri, Boolean verifyConnection) throws MalformedURLException, IOException, ZoneMinderAuthenticationException, ZoneMinderStreamConfigException, ZoneMinderGeneralException;
		
	//TODO:: MUST HANDLE ELSEWQHERE
	public boolean writeTelnet(String writeString) throws IOException;
	
	//TODO:: MOVE TO PROXY !!!
	public String getHttpUrl();
	public int getResponseCode(); 
	public String getResponseMessage();


	
}
