package name.eskildsen.zoneminder.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

//TODO - Remove reference 
import javax.ws.rs.NotSupportedException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;

//TODO HIDE THIS Class - Something must be moved .-)
public abstract class ZoneMinderCoreData implements IZoneMinderResponse {

	private URI requestURI = null;
	private int responseCode = 0;
	private String responseMessage = "";

	protected ZoneMinderCoreData()
	{
	}
	
	protected ZoneMinderCoreData(int status, String message, URI uri)
	{
		requestURI = uri;
		responseCode = status;
		responseMessage = message;
	}
	
	
	
	public void create(JsonObject jsonObject) throws ZoneMinderInvalidData {
		if (jsonObject!=null) {
			loadJsonData(jsonObject);
		}
	}
	
	
	public void setRequestURI(URI uri) {
		this.requestURI = uri;
		
	}
	
	public void setResponseMessage(String message) {
		this.responseMessage= message;
	}
	
	public void setResponseStatus(int status) {
		this.responseCode = status;
	}

	
	
	@Override
	public int getHttpStatus() {return responseCode;}
	
	//public void setHttpResponseCode(int rc) {responseCode = rc;}

	@Override
	public String  getHttpResponseMessage() {return responseMessage;}


	@Override
	public String getHttpRequestUrl() {
		return requestURI.toString();
	}
	
	@Override
	public URI getHttpRequestURI() {
		return requestURI;
	}

	
	@Deprecated
	public static <T> T createFromJson_OLD(JsonObject jsonObject, Class<T> classOfType)
	{
		ZoneMinderCoreData zmrd = null;
		T data = null;
		try {
			data = classOfType.newInstance();
			
			if (ZoneMinderCoreData.class.isAssignableFrom(data.getClass())) {

				zmrd = (ZoneMinderCoreData)data;
				zmrd.loadJsonData(jsonObject);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//TODO FIX THIS
			e.printStackTrace();
		}
		return data;
	}
	
	private Object getFieldValueFromJsonElement(Field field, JsonElement element) {
	
		if (element.isJsonNull()) {
			return null;
		}
		else if (field.getType() == String.class) {
			return element.getAsString();
		}
		else if (field.getType() == Boolean.class) {
			return element.getAsBoolean();
		}
		else if (field.getType() == Integer.class) {
			return element.getAsInt();
		}
		
		else if (field.getType() == List.class) {
			List<?> list = null;
			JsonElement je = null;
				if (element.isJsonArray())
			{
				je = element.getAsJsonArray().get(0);
			}
				else {
					throw new NotSupportedException("JSON data not supported (1)");
				}
			
			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)field.getGenericType();
				Type elementType = pt.getActualTypeArguments()[0];
				
				
				
				if ((elementType==Boolean.class) && (je.getAsJsonPrimitive().isBoolean())) {
					System.out.println("0");
					throw new NotSupportedException("DATA NOT SUPPORTED");
				}
				else if ((elementType==String.class) && (je.getAsJsonPrimitive().isString())) {
					System.out.println("1");
					throw new NotSupportedException("DATA NOT SUPPORTED");
				} 
				else if ((elementType==Float.class) && (je.getAsJsonPrimitive().isNumber())) {
					List<Float>listFloat = new ArrayList<Float>();
					for (JsonElement e : element.getAsJsonArray()) {
						listFloat.add(e.getAsFloat());
					}
					return listFloat;
					
				}
				else {
					System.out.println("2");
					throw new NotSupportedException("DATA NOT SUPPORTED");
				}
			}
		}
		else if(field.getType() == Object.class) {
			//TODO:: Implement this (used in monitors.json
		}

		else {
		
			//TODO:: FIX THIS GSON STuff
			String message1 = String.format("'createFromJson' requested to load an unsupported data type (Field='{}', Json='{}'", field.getType().getName(), element.toString());
			System.out.println(message1);
			throw new NotSupportedException();
		}			
		return null;
		
	}
	
	//protected abstract void loadJson(JsonObject jsonObject);
		
	private void loadJsonData(JsonObject jsonObject) { 

		for(Field field : this.getClass().getDeclaredFields())
		{
			Field f1 = field;
		    if (field.isAnnotationPresent(SerializedName.class))
	        {
//		    	String message = String.format("Processing field '%s'", field.getName());
//		    	System.out.println(message);
			    //do action
		    	SerializedName sn = (SerializedName)(field.getAnnotation(SerializedName.class));
		    	String value = sn.value();
		    	String valueJson = "";
		    	Object curType = null;
		    		
	    		JsonElement element = jsonObject.get(value);

	    		//TODO:: Handle THIS
	    		if (element!=null) {
		    		curType  = field.getType();
	    			try {
	    				field.setAccessible(true);
	    				
	    				field.set(this, getFieldValueFromJsonElement(field, element));
	    			} catch (IllegalArgumentException | IllegalAccessException e) {
						//TODO:: FIX THIS
	    				// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("3");
					}

	    		}
		    }
		}

	}


	public String getKey() {
		return this.getClass().getSimpleName();
	}	


}
