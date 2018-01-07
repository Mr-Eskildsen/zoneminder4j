package name.eskildsen.zoneminder.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.ZoneMinderResponseData;
import name.eskildsen.zoneminder.api.ZoneMinderDiskUsage;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.trigger.ZoneMinderEventNotifier;


//TODO:: THIS IS IN USE CLEAN UP !!!


public class ZoneMinderGenericProxy implements IZoneMinderResponse {


	protected static final String QUERY_CURRENTPAGE = "page={currentPage}";
    protected static final String QUERY_CONFIG_UPDATE = "Config[Value]={configValue}";
    
	protected static final String DAEMON_NAME_CAPTURE = "zmc";
	protected static final String DAEMON_NAME_ANALYSIS = "zma";
    protected static final String DAEMON_NAME_FRAME = "zmf";

	protected JsonParser parser = new JsonParser();
    protected Gson gson = new Gson();

	private String _id = "";

	private String url = "";
	private int responseCode = 0;
	private String responseMessage = "";
	
	private ZoneMinderSession  _session = null;
	public ZoneMinderGenericProxy(IZoneMinderSession session) {
		_session = (ZoneMinderSession)session;
	
	}
	
	public ZoneMinderGenericProxy(ZoneMinderSession session) {
		_session = (ZoneMinderSession)session;
	
	}
	
	protected String getId()
	{
		return _id;
	}
	
	protected void setId(String id)
	{
		_id = id;
	}

	
	protected IZoneMinderSession getSession() {
		return _session;
	}


	protected void releaseSession(ZoneMinderSession session) {
		
		setHttpResponse(session);
	}

	
	protected ZoneMinderSession aquireSession() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException
	{
		if (_session!=null) {
			return _session;
		}
		return null;
	}

protected ZoneMinderResponseData convertToClass(JsonObject object, Type classType){
	ZoneMinderResponseData data = gson.fromJson(object, classType);
	
	if (data==null) {
		try {
			data = (ZoneMinderResponseData)(Class.forName(classType.getTypeName()).newInstance());
	
		} catch (InstantiationException | IllegalAccessException |ClassNotFoundException e) {
			data = null;
		}
	}
	
	if (data!=null) {
		data.setHttpUrl(getHttpUrl());
		data.setHttpResponseCode(getHttpResponseCode());
		data.setHttpResponseMessage(getHttpResponseMessage());
	}
	return data;
}
	
    private URI BuildURI(IZoneMinderConnectionInfo connectionInfo, String methodPath) throws MalformedURLException
    {
	    return BuildURI(connectionInfo, methodPath, null);
	}

    
    private URI BuildURI(IZoneMinderConnectionInfo connectionInfo, String methodPath, String queryString) throws MalformedURLException
    {
    	String s = "";
    	
    	
	    // Build Path to the required method in the API
	    UriBuilder uriBuilder= null;
	    //connectionInfo.getZoneMinderRootUri()).path(ZoneMinderServerConstants.SUBPATH_API)
		uriBuilder = UriBuilder.fromUri(connectionInfo.getZoneMinderApiBaseUri()).path(methodPath);

	    if ((queryString != null) && (queryString != "")) {
	        uriBuilder = uriBuilder.replaceQuery(queryString);
    	}
	    return uriBuilder.build();
	}
    
    protected String resolveCommands(String url, String command, String commandValue) {
        String commandKey = "{" + command + "}";
        if (url.contains(commandKey)) {
            url = url.replace(commandKey, commandValue);
        }
        return url;
    }

    
    	

		/**
	     * 
	     * HTTP Access (Put
	     * */
	    protected String sendPut(String methodPath, String postParams) throws Exception {
	    	ZoneMinderSession session = aquireSession();
	    	
	    	String t = String.format("Call to '%s' with params '%s' failed", methodPath, postParams);
	    	if (session==null){
	    		throw new NullPointerException(String.format("Call to '%s' with params '%s' failed", methodPath, postParams));
	    	}
	    	String result = session.sendRequest(BuildURI(session.getConnectionInfo(), methodPath), HttpRequest.PUT, postParams);
	    	setHttpResponse(session);
	    	releaseSession( session );

	    	return result;
	    }
	    
	    
	    /**
	     * 
	     * HTTP Access (Post
	     * */
	    
	    protected String sendPost(String methodPath, String postParams) throws Exception {
	    	ZoneMinderSession session = aquireSession();
	    	String result = session.sendRequest(BuildURI(session.getConnectionInfo(), methodPath), HttpRequest.POST, postParams);
	    	setHttpResponse(session);
	    	releaseSession( session );

	    	return result;
	    }
	    
	    
	   
	    protected JsonObject getAsJson(String methodPath) throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException {
	        return getAsJson(methodPath, null);
	    }

	    protected JsonObject getAsJson(String methodPath, String queryString) throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException {
	    	JsonObject object = null;
	    	ZoneMinderSession session = null;
	    	URI uri = null;
	    	String result = "";
	    	try {
	    		session = aquireSession();
	    		if (session==null) {
	    	    	throw new NullPointerException(String.format("Call to '%s' with queryString '%s' failed", methodPath, queryString));
	    		}
	    		
	    		uri = BuildURI(session.getConnectionInfo(), methodPath, queryString);
	    		result = session.getDocumentAsString(uri, true);
	    		if (result.equals("")) {
	    			return null;
	    		}
	    		JsonElement element = parser.parse(result);
	    		object = element.getAsJsonObject();
	        }
	    	
	        catch(IOException  ioe)
	        {
	        	throw ioe;
	        }
	    
    	
    	finally{
    		releaseSession( session );
    	}

	        return object;
	    }

	    
	    private String getAsString(String methodPath) throws Exception {
	    	ZoneMinderSession session = aquireSession();
	    	try {
	    		if (session==null) {
	    	    	throw new NullPointerException(String.format("getAsString(): Call to '%s' failed", methodPath));
	    		}

	    		return session.getDocumentAsString(BuildURI(session.getConnectionInfo(), methodPath, null), true);
	    	}
	    	
	    	finally {
	    		if (session !=null){
	    			setHttpResponse(session);
	    			releaseSession( session );
	    		}
	    	}
	        
	    }

	    private String getAsString(String methodPath, String queryString) throws Exception {
	    	
	    	ZoneMinderSession session = aquireSession();
	    	try {
	    		if (session==null) {
	    	    	throw new NullPointerException(String.format("getAsString(): Call to '%s' with queryString '%s' failed", methodPath, queryString));
	    		}
    			return session.getDocumentAsString(BuildURI(session.getConnectionInfo(), methodPath, queryString), true);
	    	}
	    	finally {
	    		if (session !=null){
	    			setHttpResponse(session);
	    			releaseSession( session );
	    		}
	    	}
    	}

		
		@Override
	    public String getHttpUrl() {
			return url;
		}

		@Override
		public int getHttpResponseCode() {
			return responseCode;
		}
		

		@Override
		public String getHttpResponseMessage() {
			return responseMessage;
		}


		protected void setHttpResponse(ZoneMinderSession session) {
			if (session!=null) {
				url = session.getHttpUrl();
				responseCode = session.getResponseCode();
				responseMessage = session.getResponseMessage();
			}
		}






}
