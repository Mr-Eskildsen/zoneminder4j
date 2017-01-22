package name.eskildsen.zoneminder.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderSession;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.general.ProtocolType;
import name.eskildsen.zoneminder.general.ZoneMinderWelcomePageEnum;
import name.eskildsen.zoneminder.trigger.ZoneMinderEventNotifier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import javax.security.auth.login.FailedLoginException;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

//TODO:: Fix so that we can hide this class

public class ZoneMinderSession implements IZoneMinderSession {

	private final String USER_AGENT = "Mozilla/5.0";
	private IZoneMinderConnectionInfo _connection = null;
	
    private boolean _connectHttp = false;
    private boolean _connectTelnet = false;
	
    


	protected static final String QUERY_CURRENTPAGE = "page={currentPage}";
    protected static final String QUERY_CONFIG_UPDATE = "Config[Value]={configValue}";
    
	protected static final String DAEMON_NAME_CAPTURE = "zmc";
	protected static final String DAEMON_NAME_ANALYSIS = "zma";
    protected static final String DAEMON_NAME_FRAME = "zmf";

	protected JsonParser parser = new JsonParser();
    protected Gson gson = new Gson();

	
	/**
	 * HTTP Connection
	 */
	private boolean connectedHttp = false;
	private int responseCode;
    private String responseMessage;
    
    private List<String> cookies;
    
    
    public ZoneMinderSession(IZoneMinderConnectionInfo connection, boolean connectHttp, boolean connectTelnet) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException{
    	_connection = connection;  
    	_connectHttp = connectHttp;
    	_connectTelnet = connectTelnet;
    	
    	
    	if (connectHttp)
    		connectHttp();
    	
    	if (connectTelnet)
    		connectTelnet();
    	
    }

    
    
    public IZoneMinderConnectionInfo getConnectionInfo() {return _connection;}

    public boolean isConnected() {
    	
    	if (_connectHttp && _connectTelnet) {
    		return (isConnectedHttp() && isConnectedTelnet());
    	}
    	else if (_connectHttp) {
    		return (isConnectedHttp());
    	}	
    	else if (_connectTelnet) {
    		return (isConnectedTelnet());
    	}
    	return false;
    }

    protected boolean isConnectedHttp()
    {
    	return this.connectedHttp;
    }
    
    
    protected void setConnectedHttp(boolean connected) throws IOException {
    	boolean stateChanged = false;
    	
    	/*
		try {
			if (lock.tryLock(5L, TimeUnit.SECONDS)) {
	    		if (this.connectedHttp != connected)
	    			stateChanged = true;
	    		this.connectedHttp = connected;	
			}
		} catch (InterruptedException e) {

		} finally {
	        lock.unlock();
	    }

		*/
	
    	synchronized (this) {
    		if (this.connectedHttp != connected)
    			stateChanged = true;
    		this.connectedHttp = connected;	
		} 
  	
    	if (stateChanged) {
    		if (!connected) {
    			closeConnection();
    		}
    	}
        
    }

    public boolean connect() throws IllegalArgumentException, IOException, FailedLoginException, ZoneMinderUrlNotFoundException {
    	boolean resultTelnet =  false; 
    	boolean resultHttp =  false; 
    	
    	
    	if (_connectTelnet)
    		resultTelnet = connectTelnet();
    	else 
    		resultTelnet = true;
    	
    	if (_connectHttp)
    		resultHttp = connectHttp();
    	else
    		resultHttp = true;
    	
    	return resultHttp && resultTelnet;
    	
    }
    
    private boolean connectHttp() throws IllegalArgumentException, IOException, FailedLoginException, ZoneMinderUrlNotFoundException {

        
    	try {

			
			setConnectedHttp(false);		
		
			if (getConnectionInfo()==null)
				return isConnectedHttp();
			
	        // make sure cookies is turn on
	        CookieHandler.setDefault(new CookieManager());
	        
	        // Make sure we are logged in to ZoneMinder
	        setConnectedHttp( ensureLogin(ZoneMinderServerConstants.SUBPATH_SERVERLOGIN, getConnectionInfo().getUserName(), getConnectionInfo().getPassword()));
	
    	/*
    		synchronized (this) {
        		
        		setConnectedHttp(false);		
			
        		if (getConnectionInfo()==null)
        			return isConnectedHttp();
        		
	            // make sure cookies is turn on
	            CookieHandler.setDefault(new CookieManager());
	            
	            // Make sure we are logged in to ZoneMinder
	            setConnectedHttp( ensureLogin(ZoneMinderServerConstants.SUBPATH_SERVERLOGIN, getConnectionInfo().getUserName(), getConnectionInfo().getPassword()));
	
        	}
        	*/
    	} catch(IllegalArgumentException iae) {
    		setConnectedHttp(false);
    		throw iae;
    	}
    	catch(UriBuilderException ube) {
    		setConnectedHttp(false);
    		throw ube;
    	}
    	catch(IOException ioe) {
    		setConnectedHttp(false);
    		throw ioe;
    	}
        return isConnectedHttp();
    }

  
	
	
	private void disconnectHttp() throws IOException {
		setConnectedHttp(false);
		cookies = null;
	}
	

    
    public void closeConnection() throws IOException {
    	disconnectHttp();
    	disconnectTelnet();
    }


    private static String TAG_LOGGING_IN_PAGE = "Logging in";
    private static String TAG_LOGIN_PAGE = "ZoneMinder Login";
    private static String TAG_START_PAGE = "- Console";

    protected ZoneMinderWelcomePageEnum getFirstPageType(String html) {
        Document doc = Jsoup.parse(html);

        // Seems like we must provide user credentials
        if ((doc.getElementsContainingText(TAG_LOGIN_PAGE).size() > 0) && (doc.getElementById("loginForm") != null)) {
            return ZoneMinderWelcomePageEnum.LOGIN_PAGE;
        }
        // We are trying to login, don't know the result yet :-)
        else if (doc.getElementsContainingText(TAG_LOGGING_IN_PAGE).size() > 0) {
            return ZoneMinderWelcomePageEnum.LOGGING_IN_PAGE;
        }
        // Finally check if we ended on the startpage
        else if (doc.title().endsWith(TAG_START_PAGE)) {
            return ZoneMinderWelcomePageEnum.START_PAGE;
        }
        return ZoneMinderWelcomePageEnum.UNKNOWN_PAGE;
    }

    
    protected Boolean ensureLogin(String methodPath, String username, String password) throws MalformedURLException, IllegalArgumentException, IOException, FailedLoginException, ZoneMinderUrlNotFoundException {

	    UriBuilder uriLogin = UriBuilder.fromUri(getConnectionInfo().getZoneMinderRootUri()).path(methodPath);

    	// Fetch page
        String html = _getDocumentAsString(uriLogin.build(), false);

        ZoneMinderWelcomePageEnum pageType = getFirstPageType(html);

        // We didn't hit the login form. Server is probably not protected by login
        if (pageType == ZoneMinderWelcomePageEnum.LOGIN_PAGE) {

            // Get the Document
            Document doc = Jsoup.parse(html);

            // Lets see if we got a ZoneMinder FormLogin id
            Element loginForm = doc.getElementById("loginForm");

            // Lets do the magic....
            Elements inputElements = loginForm.getElementsByTag("input");
            List<String> paramList = new ArrayList<String>();
            for (Element inputElement : inputElements) {
                Elements cur = inputElement.getElementsByTag("input");
                String type = inputElement.attr("type");
                String key = inputElement.attr("name");
                String value = inputElement.attr("value");
                if (!type.equals("submit")) {
                    if (key.equals("username")) {
                        value = username;
                    } else if (key.equals("password")) {
                        value = password;
                    }
                    paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
                }
            }

            String parameters = BuildPostParameterString(paramList);

            Integer retries = 0;
            // Trying to login
            String responseHtml = _sendRequest(uriLogin.build(), HttpRequest.POST, parameters);
            while ((getFirstPageType(responseHtml) == ZoneMinderWelcomePageEnum.LOGGING_IN_PAGE) && (retries < 10)) {
            	
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//We really doesn't care if that shoul dhappens 
				}

                // Keep trying
                responseHtml = _getDocumentAsString(uriLogin.build(), false);
                retries++;
            }

            if (getFirstPageType(responseHtml) == ZoneMinderWelcomePageEnum.START_PAGE) {
                setConnectedHttp(true);
            } else {

            	setConnectedHttp(false);
                throw new FailedLoginException("Provided credentials not valid");
            }
            
        }
        // If we land directly on the main form login isn't activated in the config -> just continue :-)
        else if (pageType == ZoneMinderWelcomePageEnum.START_PAGE) {
            // Set isConnnected Boolean to True
        	setConnectedHttp(true);
        }
        // If we didn't land on the main form something is wrong
        else {
        	setConnectedHttp(false);
            throw new ZoneMinderUrlNotFoundException(uriLogin);
            
        }

        return isConnectedHttp();
    }

    
    public String BuildPostParameterString(List<String> paramList) {
    
    	// build parameters list
    	StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
    }

    private List<String> getCookies() {
        return cookies;
    }

    private void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }

    public String _getDocumentAsString(URI uri, Boolean verifyConnection) throws MalformedURLException, IOException {

    	HttpURLConnection httpConnection =  null;
    	
        if ((verifyConnection) && (isConnectedHttp() == false)) {
            return "";
        }

        StringBuffer response = new StringBuffer();

		
        httpConnection = (HttpURLConnection) uri.toURL().openConnection();
        
        // Set Connection timeout
        httpConnection.setConnectTimeout(getConnectionInfo().getTimeout());

        
        // default is GET
        httpConnection.setRequestMethod("GET");
        httpConnection.setUseCaches(false);

        // act like a browser
        httpConnection.setRequestProperty("User-Agent", USER_AGENT);
        httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        if (getCookies() != null) {
            for (String cookie : getCookies()) {
            	httpConnection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }

        
        responseCode = httpConnection.getResponseCode();
        responseMessage = httpConnection.getResponseMessage();
        if (responseCode == 200) {

            BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // Update the cookies
            setCookies(httpConnection.getHeaderFields().get("Set-Cookie"));
        } else {
            String message = "";
            switch (responseCode) {
                case 404:
                    message = String.format(
                            "The URL '%s' was not found on ZoneMinder Server. Please verify that the server is accessible, and that your OpenHAB Bridge configuration is correct. (ResponseCode='%d', ResponseMessage='%s')",
                            uri.toString(), responseCode, httpConnection.getResponseMessage());
                    break;
                default:
                    message = String.format(
                            "An error occured while communicating with ZoneMinder Server: URL='%s', ResponseCode='%d', ResponseMessage='%s'",
                            uri.toString(), responseCode, httpConnection.getResponseMessage());
            }
            // TODO:: Fix Me logger.error(message);
        }
        return response.toString();

    }

    
    public String _sendRequest(URI uri, HttpRequest Request, String postParams) throws MalformedURLException, IOException {
    	HttpURLConnection httpConnection =  null;
    	

		httpConnection = (HttpURLConnection) uri.toURL().openConnection();

        // Acts like a browser
        httpConnection.setUseCaches(false);

        httpConnection.setConnectTimeout(getConnectionInfo().getTimeout());
        
        httpConnection.setRequestMethod(Request.name());
        httpConnection.setRequestProperty("Host", uri.toURL().getHost());
        httpConnection.setRequestProperty("User-Agent", USER_AGENT);
        httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        
        if (getCookies() != null) {
        	for (String cookie : getCookies()) {
        		//httpConnection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        		httpConnection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
        	}
        }
        
        httpConnection.setRequestProperty("Connection", "keep-alive");
        httpConnection.setRequestProperty("Referer", uri.toURL().toString());
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConnection.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
        
        int responseCode = httpConnection.getResponseCode();

        if (responseCode == 200) {

            BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            String message = "";
            switch (responseCode) {
                case 404:
                    message = String.format(
                            "The URL '%s' was not found on ZoneMinder Server. Please verify that the server is accessible, and that your openHAB Bridge configuration is correct. (ResponseCode='%d', ResponseMessage='%s')",
                            uri.toString(), responseCode, httpConnection.getResponseMessage());
                    break;
                default:
                    message = String.format(
                            "An error occured while communicating with ZoneMinder Server: URL='%s', ResponseCode='%d', ResponseMessage='%s'",
                            uri.toString(), responseCode, httpConnection.getResponseMessage());
            }
        }
        return null;

    }
	
    public int getHttpResponseCode() {return responseCode;}
    public void setHttpResponseCode(int rc) {responseCode = rc;}

    public String getHttpResponseMessage() {return responseMessage;}
    public void setHttpResponseMessage(String rm) {responseMessage = rm;}
    /*******************************************************
     * 
     *	TCP
     * 
     *******************************************************/
    /**
	 *	Telnet connection 
	 */
	private Socket telnetSocket = null;
	private PrintWriter telnetOutput = null;
	private BufferedReader telnetInput = null;

	
    private static final int TELNET_TIMEOUT = 1000;

    protected boolean isConnectedTelnet()
    {
    	if (telnetSocket==null)
    		return false;
    	return telnetSocket.isConnected();
    }
    
    
    protected boolean connectTelnet() throws IOException {
   
    	telnetSocket = new Socket();
        SocketAddress TPIsocketAddress = new InetSocketAddress(getConnectionInfo().getHostName(), getConnectionInfo().getTelnetPort());
        telnetSocket.connect(TPIsocketAddress, TELNET_TIMEOUT);
        telnetInput = new BufferedReader(new InputStreamReader(telnetSocket.getInputStream()));
        telnetOutput = new PrintWriter(telnetSocket.getOutputStream(), true);
    	return true;
    }
    
    protected void disconnectTelnet() throws IOException
    {
    	if (telnetSocket!=null) {
    		telnetSocket.close();
    		telnetSocket = null;
    	}

    	if (telnetInput!=null) {
    		telnetInput.close();
    		telnetInput = null;
    	}
    	
    	if (telnetOutput!=null) {
    		telnetOutput.close();
    		telnetOutput = null;
    	}
    
    }
    
   public boolean writeTelnet(String writeString) throws IOException {

	   if (!isConnectedTelnet())
		   connectTelnet();
	   
	   if (!isConnectedTelnet())
		   return false;
	   
       telnetOutput.write(writeString);
       telnetOutput.flush();
       return true;
       
   }

   /**
 * @throws IOException 
   *
   **/
  public String pollTelnet() throws IOException  {
      String message = "";
      try {
    	  if (!isConnectedTelnet())
    		  connectTelnet();
	   
    	  if (!isConnectedTelnet())
    		  return "";

          message = telnetInput.readLine();
          
      } catch (SocketTimeoutException stException) {
          // We known it is comnming, so just ignore this
      }
      
      return message;
  }
   
}
