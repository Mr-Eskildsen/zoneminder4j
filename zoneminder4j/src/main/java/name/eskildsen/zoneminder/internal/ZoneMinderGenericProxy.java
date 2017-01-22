package name.eskildsen.zoneminder.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.ZoneMinderData;
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

protected ZoneMinderData convertToClass(JsonObject object, Type classType){
	ZoneMinderData data = gson.fromJson(object, classType);
	
	if (data==null) {
		try {
			data = (ZoneMinderData)(Class.forName(classType.getTypeName()).newInstance());
	
		} catch (InstantiationException | IllegalAccessException |ClassNotFoundException e) {
			data = null;
		}
	}
	
	if (data!=null) {
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
			uriBuilder = UriBuilder.fromUri(connectionInfo.getZoneMinderRootUri()).path(ZoneMinderServerConstants.SUBPATH_API)
			        .path(methodPath);

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
	    	String result = session._sendRequest(BuildURI(session.getConnectionInfo(), methodPath), HttpRequest.PUT, postParams);
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
	    	String result = session._sendRequest(BuildURI(session.getConnectionInfo(), methodPath), HttpRequest.POST, postParams);
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
	    		uri = BuildURI(session.getConnectionInfo(), methodPath, queryString);
	    		result = session._getDocumentAsString(uri, true);
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
	    		return session._getDocumentAsString(BuildURI(session.getConnectionInfo(), methodPath, null), true);
	    	}
	    	finally {
	    		setHttpResponse(session);
	    		releaseSession( session );
	    	}
	        
	    }

	    private String getAsString(String methodPath, String queryString) throws Exception {
	    	
	    	ZoneMinderSession session = aquireSession();
	    	try {
    			return session._getDocumentAsString(BuildURI(session.getConnectionInfo(), methodPath, queryString), true);
	    	}
	    	finally {
	    		setHttpResponse(session);
	    		releaseSession( session );
	    	}
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
			responseCode = session.getHttpResponseCode();
			responseMessage = session.getHttpResponseMessage();
		}





}
