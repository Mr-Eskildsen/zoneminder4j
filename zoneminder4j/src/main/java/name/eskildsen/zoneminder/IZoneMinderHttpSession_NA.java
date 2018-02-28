package name.eskildsen.zoneminder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;

import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;

//IS Referenced

@Deprecated
public interface IZoneMinderHttpSession_NA extends IZoneMinderCoreSession {

	

	boolean isAuthenticated();
	boolean isConnected();
	
	@Deprecated
	ByteArrayOutputStream getAsByteArray(URI uri, ArrayList<JettyQueryParameter> parameters, Boolean verifyConnection)
			throws MalformedURLException, IOException, ZoneMinderAuthenticationException,
			ZoneMinderStreamConfigException, ZoneMinderGeneralException;

	
	
	
		
}
