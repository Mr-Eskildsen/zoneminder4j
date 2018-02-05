package name.eskildsen.zoneminder.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderHttpSession;

import name.eskildsen.zoneminder.api.ZoneMinderCoreData;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
import name.eskildsen.zoneminder.jetty.HttpCore;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;


//TODO THIS IS IN USE CLEAN UP !!!


public abstract class ZoneMinderGenericProxy extends HttpCore /*implements IZoneMinderResponse*/ {


	protected static final String QUERY_CURRENTPAGE = "page={currentPage}";
    protected static final String QUERY_CONFIG_UPDATE = "Config[Value]={configValue}";
    
	protected static final String DAEMON_NAME_CAPTURE = "zmc";
	protected static final String DAEMON_NAME_ANALYSIS = "zma";
    protected static final String DAEMON_NAME_FRAME = "zmf";

	protected JsonParser parser = new JsonParser();
	//TODO Reenable Gson
    //private Gson gson = new Gson();

	private String _id = "";

	private String url = "";
	private int responseCode = 0;
	private String responseMessage = "";

	private String zmsnphPath = "";

	//@Deprecated
	//private IZoneMinderHttpSessionInternal  _session = null;
	
	private IZoneMinderConnectionHandler _connection = null;
	/*
	@Deprecated
	public ZoneMinderGenericProxy(HttpSessionCore httpSessionCore, boolean useCore) {
		_session = (IZoneMinderHttpSessionInternal)httpSessionCore;
		this._connection = _session.getConnectionInfo(); 
		//if (this._connection.getClass().isAssignableFrom(JettyConnectionHandler))
	}
	
	@Deprecated
	public ZoneMinderGenericProxy(IZoneMinderHttpSession session) {
		_session = (IZoneMinderHttpSessionInternal)session;
		this._connection = _session.getConnectionInfo();
	}
*/
	public ZoneMinderGenericProxy(IZoneMinderConnectionHandler connection) {
		this._connection = connection;
	
	}

	
	protected IZoneMinderConnectionHandler getConnection() {
		return _connection;
	}
	protected String getId()
	{
		return _id;
	}
	
	protected void setId(String id)
	{
		_id = id;
	}


    protected URI buildUriApi(String path) throws MalformedURLException {
    	return buildURI(getConnection().getApiUri(), path);
    }

    protected URI buildUriZmsNph(String path) throws MalformedURLException {
    	return buildURI(getConnection().getZmsNphUri(), path);
    }

    
    protected ZoneMinderContentResponse get(SiteTypeEnum site, String methodPath, List<JettyQueryParameter> parameters) throws  ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException {
    	URI uri = null;
    	
    	switch(site) {
    	case Portal:
    		uri = buildURI(getConnection().getPortalUri(), methodPath);
    		break;
    	case API:
    		uri = buildUriApi(methodPath);
    		break;
    	case CgiBin:
    		uri = buildUriZmsNph(methodPath);
    		break;
    	default:
    		//TODO Better EXCEPTION
    		throw new ZoneMinderGeneralException("No valid ZoneMinder Site given", null); 
        }
    	
    	return  getConnection().fetchContentResponse(uri, HttpMethod.GET, parameters);
    }

    public boolean isConnected()
    {
    	return getConnection().isConnected();
    }

    

    /*
    protected URI buildZmsNphURI() throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException
    {
    	return buildZmsNphURI(null);
    }
    */
    

    /////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
	/*
	protected void releaseSession(IZoneMinderHttpSessionInternal session) {
		
		setHttpResponse(session);
	}
*/
	/*
	protected IZoneMinderHttpSessionInternal aquireSession() throws IOException, ZoneMinderUrlNotFoundException
	{
//		if (_session!=null) {
//			return _session;
		}
		
		throw new UnsupportedOperationException("aquireSession is Deprecated");
	}
*/
	/*
	@Deprecated
	protected <T extends ZoneMinderCoreData> T convertToClass(JsonObject object, Class<T> classOfT) throws ZoneMinderInvalidData{
		T data = null;

		data = ZoneMinderCoreData.createFromJson(object, classOfT);
		
		if (data!=null) {
			((ZoneMinderCoreData)data).setHttpUrl(getHttpUrl());
			((ZoneMinderCoreData)data).setHttpResponseCode(getHttpResponseCode());
			((ZoneMinderCoreData)data).setHttpResponseMessage(getHttpResponseMessage());
		}

		return data;
	}
	*/


    /*private URI buildApiURI(IZoneMinderConnectionInfo connectionInfo, String methodPath) throws MalformedURLException
    {
	    return buildApiURI(connectionInfo, methodPath, null);
	}


    @Deprecated
    private URI buildApiURI(IZoneMinderConnectionInfo connectionInfo, String methodPath, String queryString) throws MalformedURLException
    {
    	String s = "";
    	
    	
	    // Build Path to the required method in the API
	    UriBuilder uriBuilder= null;

		uriBuilder = UriBuilder.fromUri(connectionInfo.getZoneMinderApiBaseUri()).path(methodPath);

	    if ((queryString != null) && (queryString != "")) {
	        uriBuilder = uriBuilder.replaceQuery(queryString);
    	}
	    return uriBuilder.build();
	}
*/
    /*
    protected URI buildZmsNphURI() throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException
    {
    	return buildZmsNphURI(null);
    }
    
    
    @Deprecated
    protected URI buildZmsNphURI(String queryString) throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException
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
*/
    //TODO use createQueryParameterListFromString instead
    @Deprecated
    protected String resolveCommands(String url, String command, String commandValue) {
        String commandKey = "{" + command + "}";
        if (url.contains(commandKey)) {
            url = url.replace(commandKey, commandValue);
        }
        return url;
    }


    
    protected List<JettyQueryParameter> createQueryParameterListFromString(String queryString) {
    	ArrayList<JettyQueryParameter>queryList = new ArrayList<JettyQueryParameter>(); 
        
    	//TODO Add something to check for various (any) error
        String[] parameters = queryString.split("&");
        for (int idx = 0; idx < parameters.length;idx++) {
        	String[] kvp = parameters[idx].split("=");
        	queryList.add(new JettyQueryParameter(kvp[0], kvp[1]));
        }
        return queryList;
    }
	

		/**
	     * 
	     * HTTP Access (Put
	     * */
/*	    protected String sendPut(String methodPath, String postParams) throws Exception {
	    	IZoneMinderHttpSessionInternal session = aquireSession();
	    	
	    	String t = String.format("Call to '%s' with params '%s' failed", methodPath, postParams);
	    	if (session==null){
	    		throw new NullPointerException(String.format("Call to '%s' with params '%s' failed", methodPath, postParams));
	    	}
	    	String result = session.sendRequest(buildApiURI(session.getConnectionInfo(), methodPath), ZoneMinderHttpRequest.PUT, postParams);
	    	setHttpResponse(session);
	    	releaseSession( session );

	    	return result;
	    }
	    
	*/    
	    /**
	     * 
	     * HTTP Access (Post
	     * */
	    
/*	    protected String sendPost(String methodPath, String postParams) throws Exception {
	    	IZoneMinderHttpSessionInternal session = aquireSession();
	    	String result = session.sendRequest(buildApiURI(session.getConnectionInfo(), methodPath), ZoneMinderHttpRequest.POST, postParams);
	    	setHttpResponse(session);
	    	releaseSession( session );

	    	return result;
	    }
	    
	*/    
	/*   @Deprecated
	    protected JsonObject getAsJson(String methodPath) throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException {
	        return getAsJson(methodPath, null);
	    }

	   @Deprecated
	    protected JsonObject getAsJson(String methodPath, List<JettyQueryParameter> parameters) throws  ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException {
	    	JsonObject object = null;
	    	IZoneMinderHttpSessionInternal session = null;
	    	URI uri = null;
	    	String result = "";
	    	try {
	    		session = aquireSession();
	    		if (session==null) {
	    			//TODO Better log
	    	    	throw new NullPointerException(String.format("Call to '%s' failed", methodPath));
	    		}
	    		
	    		//uri = buildApiURI(session.getConnectionInfo(), methodPath, queryString);
	    		uri = buildApiURI(session.getConnectionInfo(), methodPath);
	    		//result = session.getDocumentAsString(uri, true);
	    		result = _connection.getPageContentAsString(uri, parameters);
	    		
	    		if (result.equals("")) {
	    			return null;
	    		}
	    		JsonElement element = parser.parse(result);
	    		object = element.getAsJsonObject();
	        }
	    	
	        catch(IOException  ioe)
	        {
	        	throw ioe;
	        } catch (ZoneMinderAuthenticationException e) {
				// TODO Fix ths Betetr logging
				e.printStackTrace();
			}
	    
    	
    	finally{
    		releaseSession( session );
    	}

	        return object;
	    }
*/
    /*
	   @Deprecated
	    private String getAsString(String methodPath) throws Exception {
	    	IZoneMinderHttpSessionInternal session = aquireSession();
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
	    	
	    	IZoneMinderHttpSessionInternal session = aquireSession();
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

	    
	    
	  */  
	    
	    
	    
	    
	    
	    
	    /*
	    
	    protected ByteArrayOutputStream getAsByteArray(URI uri) throws ZoneMinderStreamConfigException, ZoneMinderGeneralException
	    {
	    	IZoneMinderHttpSessionInternal session = null;
			try {
				session = aquireSession();
	    		if (session==null) {
	    	    	throw new NullPointerException(String.format("getAsByteArray(): Call URI='%s' failed", uri.toString()));
	    		}
    			return session.getAsByteArray(uri, true);
			} catch (IOException | ZoneMinderUrlNotFoundException e) {
				// 	TODO ERROR HANDLÆING
				e.printStackTrace();
			} catch (ZoneMinderAuthenticationException e) {
				// TODO Auto-generated catch block
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
*/	    
		
	/*	@Override
	    public String getHttpUrl() {
			return url;
		}
*/
	    /*
		@Override
		public int getHttpStatus() {
		
			return responseCode;
		}
		
		@Deprecated
		public int getHttpResponseCode() {
			return responseCode;
		}
		

		@Override
		public String getHttpResponseMessage() {
			return responseMessage;
		}


		protected void setHttpResponse(IZoneMinderHttpSessionInternal session) {
			if (session!=null) {
				url = session.getHttpUrl();
				responseCode = session.getResponseCode();
				responseMessage = session.getResponseMessage();
			}
		}
*/

		
		/** *****************************************************
		 * 
		 * Config API
		 * @throws ZoneMinderGeneralException 
		 * @throws ZoneMinderResponseException 
		 * 
		 ***************************************************** */
	/*	@Deprecated
		public ZoneMinderConfig getConfig(ZoneMinderConfigEnum configId) throws ZoneMinderGeneralException, ZoneMinderResponseException {

			ZoneMinderConfig configData = null;
			JsonObject jsonObject = null;

	//		Gson localGson = new Gson();

			try {
				jsonObject = getAsJson(resolveCommands(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", configId.name()))
						.getAsJsonObject("config").getAsJsonObject("Config");

				configData = ZoneMinderConfig.fromJson(jsonObject);
				configData.setHttpResponseCode(getHttpResponseCode());
				configData.setHttpResponseMessage(getHttpResponseMessage());
				
			} catch (NullPointerException | ZoneMinderUrlNotFoundException | IOException e) {
				configData = null;
			}

			if (jsonObject == null) {
				return null;
			}

			return configData;
		}
*/
		/*
		
		public boolean setConfig(ZoneMinderConfigEnum configId, Boolean newValue) throws ZoneMinderGeneralException, ZoneMinderResponseException {
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

		*/
		
		/*
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

*/

}
