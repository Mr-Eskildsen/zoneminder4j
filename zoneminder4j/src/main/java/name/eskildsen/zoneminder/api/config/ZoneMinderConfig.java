package name.eskildsen.zoneminder.api.config;


import javax.annotation.Generated;
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
	
	public String getValueAsString()
	{
		return value;
	}
	
	
	public String getDataType()
	{
		return type;
	}
}
