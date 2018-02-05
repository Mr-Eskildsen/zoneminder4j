package name.eskildsen.zoneminder.jetty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.api.ZoneMinderCoreData;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.internal.GenericConnectionHandler;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.internal.ZoneMinderSessionConstants;

public class JettyConnectionInfo extends GenericConnectionHandler implements IZoneMinderConnectionHandler {
	//TODO Remove????
	private final String USER_AGENT = "Mozilla/5.0";
	private HttpClient jettyHttpClient;
	private boolean _isAuthenticated = false;

	
	public JettyConnectionInfo(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
			String password, String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException {

		super(protocol, hostName, portHttp, portTelnet, userName, password, zmPortalSubPath, zmApiSubPath, timeout);

	}
	private ZoneMinderContentResponse doItSync(String url, HttpMethod httpMethod) throws ZoneMinderGeneralException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		return fetchContentResponse(UriBuilder.fromPath(url).build(),httpMethod, null);
	}

	private ZoneMinderContentResponse doItSync(URI uri, HttpMethod httpMethod) throws ZoneMinderGeneralException, ZoneMinderResponseException {
		
		return fetchContentResponse(uri, httpMethod, null);
	}

	
	@Override
	public boolean isAuthenticated() {

		return _isAuthenticated;
	}
	//TODO Rename
	@Override
	public ZoneMinderContentResponse fetchContentResponse(URI uri, HttpMethod httpMethod, List<JettyQueryParameter> parameters) throws ZoneMinderGeneralException, ZoneMinderResponseException {

		ZoneMinderContentResponse responseHandler = new ZoneMinderContentResponse(); 
		if (!jettyHttpClient.isStarted()) {
			try {
				jettyHttpClient.start();
			} catch (Exception e) {
				throw new ZoneMinderGeneralException("Failed to (restart HTTP client", e.getCause());
			}
		}
		
		if (parameters==null) {
			parameters = new ArrayList<JettyQueryParameter>();
		}
	
		
		Request request = createRequest(uri)
								.method(httpMethod);
		
		for (int idx=0; idx<parameters.size();idx++) {
			try {
				request = request.param(parameters.get(idx).getName(), URLEncoder.encode(parameters.get(idx).getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Something intel.kligent
				e.printStackTrace();
			}
		}
		
		boolean requestDone = false;
		int retryCount = 0;
		while((requestDone==false) && (retryCount<3))
		{
			request.onResponseSuccess(responseHandler)
		
					.onResponseFailure(responseHandler)
					.send(responseHandler);
			
			
			while ( responseHandler.isRunning() )
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO handle this
					e.printStackTrace();
				}
			}
			
			try {
				switch(responseHandler.getResponse().getStatus()) {
				case 0:
					//Simply retry before giving in
					break;
				case HttpStatus.OK_200:
					requestDone = true;
					break;
				default:
					requestDone = true;
					
					Integer status = responseHandler.getResponse().getStatus(); 
					String response = responseHandler.getContentAsString();
					if (response!=null) {
						ZoneMinderResponseException responseException = null;
						try {
							
							JsonParser parser = new JsonParser();
							Gson gson = new Gson();
							
							JsonObject jsonException = parser.parse(response).getAsJsonObject();
							
							responseException = gson.fromJson(jsonException, ZoneMinderResponseException.class);
						}
						catch(Exception ex) {
							//TODO Handle any conversion errors
							throw ex;
						}
						if (responseException!=null) {
							throw responseException;
						}
					}
					else {
						
						String message = String.format("Query '%s' failed (Status='%d')", request.getURI().toString(), responseHandler.getResponse().getStatus() );
						throw new ZoneMinderGeneralException(message, null); 
					}
					break;
				}
			} catch(ZoneMinderResponseException| ZoneMinderGeneralException zmre) {
				throw zmre;
			} catch(Exception ex ) {
				//TODO HAndle this error
				ex.printStackTrace();
			}
			finally {
				//TODO Lousy, should be fixed in general!
		//		lastResponseCode = responseHandler.getHttpStatus();
	//			lastResponseMessage = responseHandler.getHttpResponseMessage();
//				lastUrl = responseHandler.getHttpURI().toString();
			}
			retryCount++;
		}
		return responseHandler;

	}
	
	@Override
	protected void onConnect() throws ZoneMinderGeneralException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, MalformedURLException {
		
		try {
			if (getProtocolType() == ProtocolType.HTTPS) {
				// Instantiate and configure the SslContextFactory
				SslContextFactory sslContextFactory = new SslContextFactory();
	
				// Instantiate HttpClient with the SslContextFactory
				jettyHttpClient = new HttpClient(sslContextFactory);
			} else {
				jettyHttpClient = new HttpClient();
			}
			
				
        	jettyHttpClient.setMaxConnectionsPerDestination(20);
	        
			try {

				// Start server
				jettyHttpClient.start();
			}
			catch(Exception ex) {
				//TODO HANDLE THIS
			}
			
			// Check if Authentication is enabled and that API is enabled
			ZoneMinderContentResponse zmResponse = null;
			try {
				
				zmResponse = doItSync(buildURI(getApiUri(), ZoneMinderServerConstants.SUBPATH_API_HOST_VERSION_JSON), HttpMethod.GET);

				//API Enabled and Authentication disabled
				if (zmResponse.getHttpStatus()== HttpStatus.OK_200) {
					setApiEnabled(true);
					setAuthenticationEnabled(false);
					setConnected(true);
				}
			
			} catch (ZoneMinderResponseException zmre) {
				
				if (zmre.getHttpStatus()==HttpStatus.UNAUTHORIZED_401) {
					setConnected(false);
		
					String message = zmre.getHttpMessage();
					
					if (message.equals(ZoneMinderSessionConstants.NOT_AUTHENTICATED)) {
						setAuthenticationEnabled(true);
						setApiEnabled(true);
					}
					else if (message.equals(ZoneMinderSessionConstants.API_DISABLED)) {
						setAuthenticationEnabled(true);
						setApiEnabled(false);
						throw new ZoneMinderApiNotEnabledException("ZoneMinder API Disabled");
					}
					else
					{
						//TODO Use original Exception
						throw new ZoneMinderGeneralException("Unknown Error occurred when checking API Status (Status='" + zmre.getHttpStatus() + "', Message='" + zmre.getHttpMessage() + "')", null);			
					}
				}
			}

			
			if (isAuthenticationEnabled()) {
				_isAuthenticated = authenticate();
			}
			else {
				_isAuthenticated = true;
			}
			
		} catch(ZoneMinderApiNotEnabledException | ZoneMinderAuthenticationException zme) {
			throw zme;
		}
		
		ZoneMinderConfig cfg = null; 
		ZoneMinderConfig cfgOptTriggers = null;
		//Allow Hash logins?
		ZoneMinderConfig cfgAuthHashLogins = null;
		//Hash relay (must be none)
		ZoneMinderConfig cfgAuthHashRelay = null;
		// Auth Hash Secret
		ZoneMinderConfig cfgAuthHashSecret = null;
			
		ZoneMinderConfig cfgAuthHashUseIps = null;
		//JsonObject json = null;
		JsonResponse jsonResponse = null;
			
		try {
			
			/**
			 * ZoneMinder Streaming Path
			 */
			jsonResponse = fetchDataAsJson( 
					buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_PATH_ZMS.name())), HttpMethod.GET, null );
			cfg = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);;
			
			setZoneMinderStreamingPath(cfg.getValueAsString());
			
			/**
			 * ZoneMidner Opt Triggers
			 */
			jsonResponse = fetchDataAsJson( 
								buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_OPT_TRIGGERS.name())), HttpMethod.GET, null );
								
			//TODO Fix hardcoded names
			cfgOptTriggers = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);
			 
			
			setTriggerOptionEnabled(cfgOptTriggers.getvalueAsBoolean());
			
			//ZoneMinderConfig.class
			//cfgAuthHashLogins 
			jsonResponse = fetchDataAsJson(
								buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_AUTH_HASH_LOGINS.name())), HttpMethod.GET, null );
			//TODO Fix hardcoded names
			cfgAuthHashLogins = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);
			setAuthenticationEnabled(cfgAuthHashLogins.getvalueAsBoolean());
			
			jsonResponse = fetchDataAsJson( 
								buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_AUTH_RELAY.name())), HttpMethod.GET, null );
			cfgAuthHashRelay = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);
			setAuthenticationHashReleayMethod(cfgAuthHashRelay.getValueAsString());
			
			
			jsonResponse = fetchDataAsJson( 
								buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_AUTH_HASH_SECRET.name())), HttpMethod.GET, null );
			cfgAuthHashSecret = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);
			
			setAuthenticationHashSecret(cfgAuthHashSecret.getValueAsString());
			
			jsonResponse = fetchDataAsJson( 
					buildURI(getApiUri(),resolvePlaceholder(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", ZoneMinderConfigEnum.ZM_AUTH_HASH_IPS.name())), HttpMethod.GET, null );
			cfgAuthHashUseIps = ZoneMinderCoreData.createFromJson(jsonResponse.getJsonObject().getAsJsonObject("config").getAsJsonObject("Config"), jsonResponse.getHttpStatus(), jsonResponse.getHttpMessage(), jsonResponse.getRequestURI(), ZoneMinderConfig.class);
			setAuthenticationHashUseIp(cfgAuthHashUseIps.getvalueAsBoolean());
			

		} catch (ZoneMinderResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZoneMinderInvalidData e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}		
	
	
    private String resolvePlaceholder(String input, String placeholder, String realValue) {
        String placeholderKey = "{" + placeholder + "}";
        if (input.contains(placeholderKey)) {
            input = input.replace(placeholderKey, realValue);
        }
        return input;
    }

    /*
     * 
     * 
     MOVED TO DATA CLASS!!!!!!!!!!!!
	//TODO This is way to go??
    //TODO Move to another class to allow same cponcept as httpclient
	private <T extends ZoneMinderCoreData> T convertJsonToZoneMinderClass(JsonObject jsonObject, Class<T> typeData) {
		T responseData = null;
		 //TODO Handle exceptions from gGson?
		Gson gson = new Gson();
		responseData = gson.fromJson(jsonObject, typeData);
		return responseData;
			
	}
	
	*/
	
	private JsonResponse fetchDataAsJson(URI uri, HttpMethod httpMethod, List<JettyQueryParameter> parameters) throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData {

		JsonObject jsonObject = null; 
		ZoneMinderContentResponse zmcr = fetchContentResponse(uri, httpMethod, parameters);
		try {
			JsonParser parser = new JsonParser();
			jsonObject = parser.parse(zmcr.getContentAsString()).getAsJsonObject();
		}
		catch(Exception ex) {
			throw new ZoneMinderInvalidData("Error occurred when converting response to class.", zmcr.getContentAsString(), ex.getCause());
		}
		
		return new JsonResponse(jsonObject, zmcr.getHttpStatus(), zmcr.getHttpResponseMessage(), zmcr.getHttpRequestURI() );
	}
	
/*	
	private <T extends ZoneMinderException> T fetchHest(String name, Class<T> type) {
		ZoneMinderGeneralException ex = new ZoneMinderGeneralException(name, null);
		return type.cast(ex);
		 
		//return type.cast(friends.get(name));
	}
	*/
/*
	private T fetch(T test) 
	{
		test.class
	}
	
*/	
	
	//TODO Read values from Server Config
		
	private static String PARAM_KEY_LOGIN_USERNAME = "username";
	private static String PARAM_KEY_LOGIN_PASSWORD = "password";
	private static String PARAM_KEY_LOGIN_ACTION = "action";
	private static String PARAM_KEY_LOGIN_VIEW = "view";
	private static String PARAM_VALUE_LOGIN_ACTION = "login";
	private static String PARAM_VALUE_LOGIN_VIEW = "console";
	
	@Override 
	protected boolean authenticate() throws ZoneMinderGeneralException, ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, MalformedURLException {
		
		ContentResponse response =  null;

		ZoneMinderContentResponse listener = new ZoneMinderContentResponse();
		// Issue a post request
		Request requestPost = jettyHttpClient
						.newRequest(buildURI(getPortalUri(), ZoneMinderServerConstants.SUBPATH_SERVERLOGIN))
						.method(HttpMethod.POST)
						.param(PARAM_KEY_LOGIN_USERNAME, getUserName())
					    .param(PARAM_KEY_LOGIN_PASSWORD, getPassword())
					    .param(PARAM_KEY_LOGIN_ACTION, PARAM_VALUE_LOGIN_ACTION)
						.param(PARAM_KEY_LOGIN_VIEW, PARAM_VALUE_LOGIN_VIEW)
		//				.timeout(30, TimeUnit.SECONDS)
						.onResponseFailure(listener)
						.onResponseSuccess(listener);
		
		requestPost.send(listener);
		while ( listener.isRunning() )
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//TODO::  Could be better
				throw new ZoneMinderGeneralException("Timeout when authenticating on ZoneMinder Server", e.getCause());
			}
		}
		
		if (listener.getHttpStatus() != HttpStatus.OK_200) {
			//TODO Get Cause object
			throw new ZoneMinderAuthenticationException("Error when authenticating on ZoneMinder", null ); 
		}
		
		response = null;
		ZoneMinderContentResponse responseHandler = null; //new ZoneMinderContentResponse();
		
		try {
			responseHandler = doItSync(buildURI(getApiUri(), ZoneMinderServerConstants.SUBPATH_API_HOST_VERSION_JSON), HttpMethod.GET);
		
		} catch (ZoneMinderResponseException zmre) {
			setConnected(false);
			if (zmre.getHttpStatus()==HttpStatus.UNAUTHORIZED_401) {
				
				//TODO set Exception from fail as cause  
				throw new ZoneMinderAuthenticationException("Unable to authenticate in ZoneMinder", null);
			}
		
		}
		
		if (responseHandler.getHttpStatus()==HttpStatus.OK_200) {
			setConnected(true);
		}
		else {
			setConnected(false);
			if (responseHandler.getHttpStatus()==HttpStatus.UNAUTHORIZED_401) {
				//TODO set Exception from fail as cause  
				throw new ZoneMinderAuthenticationException("Unable to authenticate in ZoneMinder", null);
			}
			else {
				//TODO Use original Exception
				throw new ZoneMinderGeneralException("Unknown Error occurred when checking API Status", null);			
			}
		}
			
		return isConnected();
	}
	
	
	@Override
	protected void onDisconnect() throws Throwable {
	
		jettyHttpClient.stop();
	}


	
	
	/*
	@Override
	@Deprecated
	public String getPageContent(String url) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		return getPageContentAsString(url);
	}

	@Override
	public void sendPost(String url, Map<String, String> postParams) throws ZoneMinderAuthenticationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendPost(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
*/
	/*
	@Override
	public String getHttpUrl()
	{
		return lastUrl;
	}
	@Override
	public int getResponseCode() {
		return lastResponseCode;
	}
	@Override
	public String getResponseMessage()
	{
		return lastResponseMessage;
	}

	@Override
	public String getPageContentAsString(String url)
			throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		return getPageContentAsString(url,  new ArrayList<JettyQueryParameter>());
	}

	
	@Override
	public String getPageContentAsString(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		
		return getPageContentAsString(UriBuilder.fromPath(url).build(), parameters);
	}
*/
	@Deprecated
	public ByteArrayOutputStream getPageContentAsByteArray(URI uri, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException, ZoneMinderStreamConfigException, ZoneMinderGeneralException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ContentResponse response =  null;
		//TODO Fix
		//lastUrl = url;
		ZoneMinderContentResponse contentResponse = null;
	
		try {
			contentResponse = fetchContentResponse(uri, HttpMethod.GET, parameters);
			
		} catch ( IllegalArgumentException | UriBuilderException e) {
			// TODO::Better handlings 
			e.printStackTrace();
		
		} catch (ZoneMinderResponseException zmre) {
			String message = "Unspecified error occurred when fetching image (Status='" + zmre.getHttpStatus() + "')";
			//TODO Use original Exception
			throw new ZoneMinderGeneralException(message, null);			
	
		} catch (ZoneMinderGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (contentResponse.getHttpStatus()==HttpStatus.OK_200) {
			InputStream inputStream = contentResponse.getContentAsInputStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;

			try {
				while ( (n = inputStream.read(byteChunk)) > 0 ) {
					baos.write(byteChunk, 0, n);
				}
			} catch (IOException e) {
				// TODO - Handle this exception
				e.printStackTrace();
			}

		
		//Probaly zms-nph not setup correct
		} else if (contentResponse.getHttpStatus()==HttpStatus.NOT_FOUND_404) {
			//TODO:: SET Cause
			throw new ZoneMinderStreamConfigException("ZoneMinder cgi-bin not found. Verify setup.", null);
		}
			
		return baos;

	}

	private Request createRequest(URI uri)
	{
		return jettyHttpClient.newRequest(uri);
		//.version(HttpVersion.HTTP_1_1)
		//.accept( "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		//.agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		//.timeout(10, TimeUnit.SECONDS);

		//request.method(HttpMethod.HEAD);
		//request.agent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:17.0) Gecko/20100101 Firefox/17.0");
	}
	
	
	
	@Override
	public ZoneMinderContentResponse getPageContent(URI uri) throws ZoneMinderAuthenticationException,
			ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException {
		// TODO Auto-generated method stub
		return getPageContent(uri, null);
	}

	@Override
	public ZoneMinderContentResponse getPageContent(URI uri, List<JettyQueryParameter> parameters)
			throws MalformedURLException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, ZoneMinderResponseException {

		ZoneMinderContentResponse responseHandler = null; 
		
		try {
			responseHandler = fetchContentResponse(uri, HttpMethod.GET, parameters);
		} catch (ZoneMinderResponseException zmre) {
			switch (zmre.getHttpStatus()) {
			
				case HttpStatus.NOT_FOUND_404:
					//TODO Throw exception - but don't disconnect
					throw zmre;
					
				case HttpStatus.UNAUTHORIZED_401:
					//setConnected(false);
					throw new ZoneMinderAuthenticationException("Unable to authenticate in ZoneMinder", zmre);
				default:
					//setConnected(false);
					//TODO Throw exception - 
					throw zmre;
			}
		}
		
		if (responseHandler.getHttpStatus()==HttpStatus.OK_200) {
			//setConnected(true);
		}
			
		
			
		return responseHandler;
	}
	
	@Override
	public ZoneMinderContentResponse sendPut(URI uri, List<JettyQueryParameter> parameters)
			throws MalformedURLException,
			ZoneMinderException {
		return send(uri, HttpMethod.PUT, parameters);
	}
	
	@Override
	public ZoneMinderContentResponse sendPost(URI uri, List<JettyQueryParameter> parameters)
			throws MalformedURLException,
			ZoneMinderException {
		return send(uri, HttpMethod.POST, parameters);
	}
	
	
	private ZoneMinderContentResponse send(URI uri, HttpMethod httpMethod, List<JettyQueryParameter> parameters)
				throws MalformedURLException, ZoneMinderException
	{

		ZoneMinderContentResponse responseHandler = new ZoneMinderContentResponse(); 
		if (!jettyHttpClient.isStarted()) {
			try {
				jettyHttpClient.start();
			} catch (Exception e) {
				throw new ZoneMinderGeneralException("Failed to (restart HTTP client", e.getCause());
			}
		}
		
		if (parameters==null) {
			parameters = new ArrayList<JettyQueryParameter>();
		}
	
		
		Request request = createRequest(uri)
								.method(httpMethod);
		
		for (int idx=0; idx<parameters.size();idx++) {
			request = request.param(parameters.get(idx).getName(), parameters.get(idx).getValue());
		}
		
		request.onResponseSuccess(responseHandler)
	
				.onResponseFailure(responseHandler)
				.send(responseHandler);
			
		
		while ( responseHandler.isRunning() )
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO handle this
				e.printStackTrace();
			}
		}
			
		try {
			switch(responseHandler.getResponse().getStatus()) {
			case 0:
				//Simply retry before giving in
				break;
			case HttpStatus.OK_200:
				break;
			default:
				Integer status = responseHandler.getResponse().getStatus(); 
				String response = responseHandler.getContentAsString();
				if (response!=null) {
					ZoneMinderResponseException responseException = null;
					try {
						
						JsonParser parser = new JsonParser();
						Gson gson = new Gson();
						
						JsonObject jsonException = parser.parse(response).getAsJsonObject();
						
						responseException = gson.fromJson(jsonException, ZoneMinderResponseException.class);
					}
					catch(Exception ex) {
						//TODO Handle any conversion errors
						throw ex;
					}
					if (responseException!=null) {
						throw responseException;
					}
				}
				else {
					
					String message = String.format("Query '%s' failed (Status='%d')", request.getURI().toString(), responseHandler.getResponse().getStatus() );
					throw new ZoneMinderGeneralException(message, null); 
				}
				break;
			}
		} catch(ZoneMinderResponseException| ZoneMinderGeneralException zmre) {
			throw zmre;
		} catch(Exception ex ) {
			//TODO HAndle this error
			ex.printStackTrace();
		}
		finally {
			//TODO Lousy, should be fixed in general!
			//		lastResponseCode = responseHandler.getHttpStatus();
//			lastResponseMessage = responseHandler.getHttpResponseMessage();
			//			lastUrl = responseHandler.getHttpURI().toString();
		}
		return responseHandler;
	}

	
	
	
/* * 
 * TODO FROM OLD INTERFACE 
	@Override
	public String getPageContentAsString(String url)
			throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException,
			ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	@Override
	public String getPageContentAsString(String url, List<JettyQueryParameter> parameters)
			throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException,
			ZoneMinderResponseException, IllegalArgumentException, UriBuilderException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	@Override
	public ByteArrayOutputStream getPageContentAsByteArray(String url, List<JettyQueryParameter> parameters)
			throws ZoneMinderAuthenticationException, ZoneMinderStreamConfigException, ZoneMinderGeneralException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	@Override
	public void sendPost(String url, Map<String, String> postParams) throws ZoneMinderAuthenticationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	@Override
	public void sendPost(String url, List<JettyQueryParameter> parameters) throws ZoneMinderAuthenticationException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}
	*/
}
