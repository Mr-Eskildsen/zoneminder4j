package name.eskildsen.zoneminder;

import java.net.URI;

import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.data.ZoneMinderCoreData;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;

public interface IZoneMinderResponse {
	
	URI getHttpRequestURI();
	String getHttpRequestUrl();
	int getHttpStatus();
	
    String getHttpResponseMessage();

    
	public static <T extends ZoneMinderCoreData> T createFromJson(JsonObject jsonObject, int httpStatus , String httpMessage, URI requestUri, Class<T> classOfType) throws ZoneMinderInvalidData {
		T data = null;

		try {
			data = classOfType.newInstance();
			
			data.setResponseStatus(httpStatus);
			data.setResponseMessage(httpMessage);
			data.setRequestURI(requestUri);
			
			
			if (ZoneMinderCoreData.class.isAssignableFrom(data.getClass())) {
				data.create(jsonObject);
			}
			
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new ZoneMinderInvalidData("Error occurred when laoding JSON data in class", jsonObject.toString(), ex);
		}
		return data;
	}

}
