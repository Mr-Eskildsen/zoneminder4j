package name.eskildsen.zoneminder.api.config;


import javax.annotation.Generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.api.ZoneMinderCoreData;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;

public class ZoneMinderConfig extends ZoneMinderCoreData {


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
/*
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
	
	@SerializedName("Requires")
	@Expose
	private Object requires;

*/ 
	
	@SerializedName("Category")
	@Expose
	private String category;
	
	@SerializedName("Readonly")
	@Expose
	private String readonly;

	
	public ZoneMinderConfig()
	{
		
	}

	@Override
	protected void create(JsonObject json) throws ZoneMinderInvalidData
	{
		try {
			this.id = json.get("Id").getAsString();
			this.name = json.get("Name").getAsString();
			this.value = json.get("Value").getAsString();
			this.type = json.get("Type").getAsString();
		}
		catch(Exception ex) {
			throw new ZoneMinderInvalidData(String.format("Could not load Json data (JSON='%s'(", json.toString()), json.toString(), ex);
		}
		
		try {
			this.defaultValue = json.get("DefaultValue").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		/*
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
*/
		try {
			category = json.get("Category").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
		
		try {
			readonly = json.get("Readonly").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
/*		
		try {
			config.requires = json.get("Requires").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
*/
	}
	
	@Deprecated
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
		/*
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
*/
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
/*		
		try {
			config.requires = json.get("Requires").getAsString();
		}
		catch(Exception e) {
			//Intentional left blank
		}
*/		return config;
		
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
