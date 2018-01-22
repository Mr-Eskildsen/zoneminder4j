package name.eskildsen.zoneminder.api;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotSupportedException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;

public abstract class ZoneMinderResponseData implements IZoneMinderResponse {

	private String url = "";
	private int responseCode = 0;
	private String responseMessage = "";

	protected ZoneMinderResponseData()
	{
	}
	
	protected ZoneMinderResponseData(int responseCode, String responseMessage, String requestUrl)
	{
		this.responseCode = responseCode;
		this.responseMessage= responseMessage;
		this.url = requestUrl;
		
	}
	
	public static <T> T createFromJson(JsonObject jsonObject, Class<T> classOfType)
	{
		ZoneMinderResponseData zmrd = null;
		T data = null;
		try {
			data = classOfType.newInstance();
			
			if (ZoneMinderResponseData.class.isAssignableFrom(data.getClass())) {

				zmrd = (ZoneMinderResponseData)data;
				zmrd.loadJsonData(jsonObject);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//TODO:: FIX THIS
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
	
	public String getHttpUrl() {return url;}
	public void setHttpUrl(String url) {this.url = url;}

	public int getHttpResponseCode() {return responseCode;}
	public void setHttpResponseCode(int rc) {responseCode = rc;}

	public String  getHttpResponseMessage() {return responseMessage;}
	public void setHttpResponseMessage(String  rm) {responseMessage = rm;}

	public String getKey() {
		return this.getClass().getSimpleName();
	}	


}
