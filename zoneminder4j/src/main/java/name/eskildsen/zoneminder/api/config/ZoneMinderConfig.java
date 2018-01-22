package name.eskildsen.zoneminder.api.config;


import javax.annotation.Generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.api.ZoneMinderResponseData;

public class ZoneMinderConfig extends ZoneMinderResponseData {


	@SerializedName("Id")
	@Expose
	private String id;
	
	@SerializedName("Name")
	@Expose
	private String name;
	
	@SerializedName("Value")
	@Expose
	private String value;
	
	@SerializedName("Type")
	@Expose
	private String type;
	
	@SerializedName("DefaultValue")
	@Expose
	private String defaultValue;

	@SerializedName("Hint")
	@Expose
	private String hint;
	
	@SerializedName("Pattern")
	@Expose
	private String pattern;
	
	@SerializedName("Format")
	@Expose
	private String format;
	
	@SerializedName("Prompt")
	@Expose
	private String prompt;
	
	@SerializedName("Help")
	@Expose
	private String help;

	@SerializedName("Category")
	@Expose
	private String category;
	
	@SerializedName("Readonly")
	@Expose
	private String readonly;
	
	@SerializedName("Requires")
	@Expose
	private Object requires;

	
	public ZoneMinderConfig()
	{
		
	}
	
	public static ZoneMinderConfig fromJson(JsonObject json)
	{
		
		ZoneMinderConfig config = new ZoneMinderConfig();
		
		config.id = json.get("Id").getAsString();
		config.name = json.get("Name").getAsString();
		config.value = json.get("Value").getAsString();
		config.type = json.get("Type").getAsString();
		try {
			config.defaultValue = json.get("DefaultValue").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		try {
			config.hint = json.get("Hint").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		
		try {
			config.pattern = json.get("Pattern").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		
		try {
			config.format = json.get("Format").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}

		try {
			config.prompt = json.get("Prompt").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}

		try {
			config.help = json.get("Help").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}

		try {
			config.category = json.get("Category").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		
		try {
			config.readonly = json.get("Readonly").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		
		try {
			config.requires = json.get("Requires").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		return config;
		
	}
	
	
	
	public boolean getvalueAsBoolean() {
		return getValueAsString().equals("1") ? true :false;
	}
	
	public String getValueAsString()
	{
		return value;
	}
	
	
	public String getDataType()
	{
		return type;
	}
}
