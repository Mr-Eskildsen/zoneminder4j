package name.eskildsen.zoneminder.exception;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneMinderResponseException extends ZoneMinderException {

	private JsonObject jsonObject = null;
	private Response response = null;
	
	public ZoneMinderResponseException(Response response, JsonObject json) {
		super("", null);
		
		jsonObject = json;
		this.response = response;
		
		JsonObject jsonExceptionData = jsonObject.get("data").getAsJsonObject();
		
		if (jsonExceptionData!=null) {
			try {
			Gson gson = new Gson(); 
			exceptionData = gson.fromJson(jsonObject.get("data").getAsJsonObject(), ExceptionData.class);
			}
			catch(Exception e){
				exceptionData = new ExceptionData();
			}
		}
	}


	 @Override
	 public String getMessage() {
		return String.format("%s - HttpStatus='%d', HttpMessage='%s', Request='%s', ExceptionMessage='%s'", super.getMessage(), getHttpStatus(), getHttpMessage(), getHttpRequest(),getExceptionMessage()); 
	}


	private ExceptionData exceptionData;
	
	
	
	public Integer getHttpStatus() {
		return response.getStatus();
	}

	public String getHttpMessage() {
		return response.getReason();
	}
	
	public String getHttpRequest() {
		return response.getRequest().getURI().toString();
	}
	

	public String getExceptionMessage() {
		return exceptionData.getExtendedInfo().getMessage();
	}
	
}