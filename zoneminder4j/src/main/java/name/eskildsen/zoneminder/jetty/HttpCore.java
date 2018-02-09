package name.eskildsen.zoneminder.jetty;

import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;

public class HttpCore {

	
    protected URI buildURI(URI baseUri, String methodPath) throws MalformedURLException
    {
	    // Build Path to the required method
		return UriBuilder.fromUri(baseUri).path(methodPath).build();
	}
    
    
   
    protected String replaceParameter(String path, String parameterName, String parameterValue) {
        String commandKey = "{" + parameterName + "}";
        if (path.contains(commandKey)) {
        	path = path.replace(commandKey, parameterValue);
        }
        return path;
    }


}
