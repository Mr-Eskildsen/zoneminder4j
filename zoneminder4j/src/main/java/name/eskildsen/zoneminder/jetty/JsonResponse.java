package name.eskildsen.zoneminder.jetty;

import java.net.URI;

import com.google.gson.JsonObject;

public class JsonResponse {
	private JsonObject json = null;
	private int httpStatus = -1;
	private String httpMessage = "";
	private URI requestURI = null;
	
	public JsonResponse(JsonObject json, int httpStatus, String httpMessage, URI uri)
	{
		this.json = json;
		this.httpStatus = httpStatus;
		this.httpMessage = httpMessage;
		this.requestURI = uri;
	}
	
	
	public JsonObject getJsonObject() {return json;}
	public int getHttpStatus() {return httpStatus;}
	public String getHttpMessage() {return httpMessage;}
	public URI getRequestURI() {return requestURI;}
}
