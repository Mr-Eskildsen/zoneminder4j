package name.eskildsen.zoneminder.internal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.ZoneMinderResponseData;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
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
    //private Gson gson = new Gson();

	private String _id = "";

	private String url = "";
	private int responseCode = 0;
	private String responseMessage = "";

	private String zmsnphPath = "";
	private ZoneMinderSession  _session = null;
	
	public ZoneMinderGenericProxy(HttpSessionCore httpSessionCore, boolean useCore) {
		_session = (ZoneMinderSession)httpSessionCore;
	}
	
	public ZoneMinderGenericProxy(IZoneMinderSession session) {
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
	protected <T> T convertToClass(JsonObject object, Class<T> classOfT){
		T data = null;

		data = ZoneMinderResponseData.createFromJson(object, classOfT);
		
		if (data!=null) {
			((ZoneMinderResponseData)data).setHttpUrl(getHttpUrl());
			((ZoneMinderResponseData)data).setHttpResponseCode(getHttpResponseCode());
			((ZoneMinderResponseData)data).setHttpResponseMessage(getHttpResponseMessage());
		}

		return data;
	}
	
	/*
	protected ZoneMinderResponseData convertToClass1(JsonObject object, Type classType){
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
	*/



    private URI buildApiURI(IZoneMinderConnectionInfo connectionInfo, String methodPath) throws MalformedURLException
    {
	    return buildApiURI(connectionInfo, methodPath, null);
	}

    
    private URI buildApiURI(IZoneMinderConnectionInfo connectionInfo, String methodPath, String queryString) throws MalformedURLException
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


    protected URI buildZmsNphURI() throws MalformedURLException
    {
    	return buildZmsNphURI(null);
    }
    
    protected URI buildZmsNphURI(String queryString) throws MalformedURLException
    {
    	String s = "";
    	
    	if (zmsnphPath == "") {
			ZoneMinderConfig config = getConfig(ZoneMinderConfigEnum.ZM_PATH_ZMS);
			zmsnphPath = config.getValueAsString();
    	}
    	
	    // Build Path to the required method in the Streaming Server
	    UriBuilder uriBuilder = UriBuilder.fromUri(_session.getConnectionInfo().getZoneMinderRootUri_()).path(zmsnphPath);
		
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
	    	String result = session.sendRequest(buildApiURI(session.getConnectionInfo(), methodPath), HttpRequest.PUT, postParams);
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
	    	String result = session.sendRequest(buildApiURI(session.getConnectionInfo(), methodPath), HttpRequest.POST, postParams);
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
	    		
	    		uri = buildApiURI(session.getConnectionInfo(), methodPath, queryString);
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

	    		return session.getDocumentAsString(buildApiURI(session.getConnectionInfo(), methodPath, null), true);
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
    			return session.getDocumentAsString(buildApiURI(session.getConnectionInfo(), methodPath, queryString), true);
	    	}
	    	finally {
	    		if (session !=null){
	    			setHttpResponse(session);
	    			releaseSession( session );
	    		}
	    	}
    	}

	    protected ByteArrayOutputStream getAsByteArray(URI uri)
	    {
	    	ZoneMinderSession session = null;
			try {
				session = aquireSession();
	    		if (session==null) {
	    	    	throw new NullPointerException(String.format("getAsByteArray(): Call URI='%s' failed", uri.toString()));
	    		}
    			return session.getAsByteArray(uri, true);
			} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException e) {
				// 	TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if (session !=null){
	    			setHttpResponse(session);
	    			releaseSession( session );
	    		}
	    	}
			return null;
	    
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


		
		/** *****************************************************
		 * 
		 * Config API
		 * 
		 ***************************************************** */    
		public ZoneMinderConfig getConfig(ZoneMinderConfigEnum configId) {

			ZoneMinderConfig configData = null;
			JsonObject jsonObject = null;

	//		Gson localGson = new Gson();

			try {
				jsonObject = getAsJson(resolveCommands(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", configId.name()))
						.getAsJsonObject("config").getAsJsonObject("Config");
/*
				JsonElement element = null;
				String id = "";
				String name = "";
				
				element = jsonObject.get("Id");
				id = element.getAsString();
				
				element = jsonObject.get("Name");
				name =element.getAsString();

				element = jsonObject.get("Value");
				name = element.getAsString();

				
				element = jsonObject.get("Type");
				String type = element.getAsString();
				
				element = jsonObject.get("DefaultValue");
				String default_value = element.getAsString();
				
				element = jsonObject.get("Hint");
				String hint = element.getAsString();
				
				element = jsonObject.get("Pattern");
				String pattern = element.getAsString();
				
				element = jsonObject.get("Format");
				String format = element.getAsString();
				
				element = jsonObject.get("Prompt");
				String prompt = element.getAsString();
				
				element = jsonObject.get("Help");
				String help = element.getAsString();
				
				element = jsonObject.get("Category");
				String category = element.getAsString();
				
				
				element = jsonObject.get("Readonly");
				String read_only = element.getAsString();
				
				element = jsonObject.get("Requires");
				String requires = element.getAsString();
				
				configData = gson.fromJson(jsonObject, ZoneMinderConfig.class);
				
				configData.setHttpResponseCode(getHttpResponseCode());
				configData.setHttpResponseMessage(getHttpResponseMessage());
*/
				configData = ZoneMinderConfig.fromJson(jsonObject);
				configData.setHttpResponseCode(getHttpResponseCode());
				configData.setHttpResponseMessage(getHttpResponseMessage());
	/*			
				
				Gson gsonTemp =
			            new GsonBuilder()
			            .registerTypeAdapter(ZoneMinderConfig.class, new AnnotatedDeserializer<ZoneMinderConfig>())
			            .create();

				ZoneMinderConfig configData1 = gsonTemp.fromJson(jsonObject, ZoneMinderConfig.class);
				Integer t = 10;
*/
				
			} catch (NullPointerException | FailedLoginException | ZoneMinderUrlNotFoundException | IOException e) {
				configData = null;
			}

			if (jsonObject == null) {
				return null;
			}

			return configData;
		}

		
		
		public boolean setConfig(ZoneMinderConfigEnum configId, Boolean newValue) {
			ZoneMinderConfig config = getConfig(configId);
			if (config.getDataType().equalsIgnoreCase("boolean")) {
				if (setConfig(configId, newValue.toString())) {
					config = getConfig(configId);
					if (config.getValueAsString().equalsIgnoreCase(newValue.toString())) {
						return true;
					}
				}
			}
			return false;
		}

		
		public boolean setConfig(ZoneMinderConfigEnum configId, String value) {
			boolean result = false;

			try {
				String methodPath = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_SERVER_SET_CONFIG_JSON, "ConfigId", configId.name());
				String queryString = resolveCommands(QUERY_CONFIG_UPDATE, "configValue", value);

				sendPost(methodPath, queryString);
				result = true;
			} catch (Exception e) {
				result = false;
			}
			return result;
		}



}
