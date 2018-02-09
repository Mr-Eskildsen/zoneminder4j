package name.eskildsen.zoneminder.internal;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Response.CompleteListener;
import org.eclipse.jetty.client.api.Response.ContentListener;
import org.eclipse.jetty.client.api.Response.FailureListener;
import org.eclipse.jetty.client.api.Response.SuccessListener;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;

public class ZoneMinderContentResponse extends BufferingResponseListener
		//implements SuccessListener, FailureListener, ContentListener, CompleteListener
 implements SuccessListener, FailureListener, ContentListener, CompleteListener, IZoneMinderResponse
		{ 


//extends BufferingResponseListener
//		implements SuccessListener, FailureListener, ContentListener//, IZoneMinderResponse {

	private AtomicBoolean done = new AtomicBoolean(false); 
	private Response response;
	/**
	 * logger
	 */
	// private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Callback to execute on complete response
	 */
	// private FritzAhaCallback callback;

	public Response getResponse() 
	{
		return response;
	}
	
	
	public boolean isRunning() {
		return !done.get();
	}
	/**
	 * Constructor
	 *
	 * @param callback
	 *            Callback which execute method has to be called.
	 */
	public ZoneMinderContentResponse() {

		super();
	}

	public ZoneMinderContentResponse(int buffer) {

		super(buffer);
	}

	
	@Override
	public void onSuccess(Response response) {
		//System.out.println("onSuccess (Response)" + response.getStatus() + " - "+ response.getRequest().getURI().toString());
		// logger.debug("HTTP response {}", response.getStatus());
		this.response = response;
		
	}

	@Override
	public void onFailure(Response response, Throwable failure) {
		// logger.debug("{}", failure.getLocalizedMessage());
		//System.out.println("onFailure... (Response) " + response.getStatus() + " - "+ response.getRequest().getURI().toString());
		this.response = response;
		
	}
	
	@Override
	public void onComplete(Result result) {
	
	   done.set(true);	
	}
		

	@Override
	public URI getHttpRequestURI() {
		return response.getRequest().getURI();
	}

	@Override
	public String getHttpRequestUrl() {
		return response.getRequest().getURI().toString();
	}


	@Override
	public int getHttpStatus() {
		return response.getStatus();
	}

	@Override
	public String getHttpResponseMessage() {
		
		return response.getReason();
	}

	public ByteArrayOutputStream getContentAsByteArray() throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (getHttpStatus()==HttpStatus.OK_200) {
			InputStream inputStream = getContentAsInputStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;
	
			while ( (n = inputStream.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
	
		}	
		return baos;
	}
	
	
	public JsonObject getContentAsJsonObject() {
		JsonParser parser = new JsonParser();
	
		JsonElement element = parser.parse(getContentAsString());
		return element.getAsJsonObject();
	}

}
