package name.eskildsen.zoneminder.api.monitor;

import java.lang.reflect.Field;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorSourceTypeEnum;
import name.eskildsen.zoneminder.data.IMonitorDataGeneral;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;

public class ZoneMinderMonitorData extends ZoneMinderCoreData implements IMonitorDataGeneral {

	@SerializedName("Id")
	@Expose
	private String id;
	
	@SerializedName("Name")
	@Expose
	private String name;
	@SerializedName("ServerId")
	@Expose
	private String serverId;
	
	@SerializedName("Type")
	@Expose
	private String type;
	
	@SerializedName("Function")
	@Expose
	private String function;
	
	@SerializedName("Enabled")
	@Expose
	private String enabled;
	
	@SerializedName("LinkedMonitors")
	@Expose
	private String linkedMonitors;
	
	@SerializedName("Triggers")
	@Expose
	private String triggers;
	@SerializedName("Device")
	@Expose
	private String device;
	@SerializedName("Channel")
	@Expose
	private String channel;
	@SerializedName("Format")
	@Expose
	private String format;
	
	@SerializedName("V4LMultiBuffer")
	@Expose
	private Boolean v4LMultiBuffer;
	
	@SerializedName("V4LCapturesPerFrame")
	@Expose
	private String v4LCapturesPerFrame;
	@SerializedName("Protocol")
	@Expose
	private String protocol;
	@SerializedName("Method")
	@Expose
	private String method;
	@SerializedName("Host")
	@Expose
	private String host;
	@SerializedName("Port")
	@Expose
	private String port;
	@SerializedName("SubPath")
	@Expose
	private String subPath;
	
	@SerializedName("Path")
	@Expose
	private String path;
	
	@SerializedName("Options")
	@Expose
	private String options;

	@SerializedName("User")
	@Expose
	private String user;

	@SerializedName("Pass")
	@Expose
	private String pass;
	
	@SerializedName("Width")
	@Expose
	private String width;
	
	@SerializedName("Height")
	@Expose
	private String height;
	
	@SerializedName("Colours")
	@Expose
	private String colours;
	
	@SerializedName("Palette")
	@Expose
	private String palette;
	@SerializedName("Orientation")
	@Expose
	private String orientation;
	@SerializedName("Deinterlacing")
	@Expose
	private String deinterlacing;
	@SerializedName("RTSPDescribe")
	@Expose
	private Boolean rTSPDescribe;
	@SerializedName("Brightness")
	@Expose
	private String brightness;
	@SerializedName("Contrast")
	@Expose
	private String contrast;
	@SerializedName("Hue")
	@Expose
	private String hue;
	@SerializedName("Colour")
	@Expose
	private String colour;
	@SerializedName("EventPrefix")
	@Expose
	private String eventPrefix;
	@SerializedName("LabelFormat")
	@Expose
	private String labelFormat;
	@SerializedName("LabelX")
	@Expose
	private String labelX;
	@SerializedName("LabelY")
	@Expose
	private String labelY;
	@SerializedName("LabelSize")
	@Expose
	private String labelSize;
	@SerializedName("ImageBufferCount")
	@Expose
	private String imageBufferCount;
	@SerializedName("WarmupCount")
	@Expose
	private String warmupCount;
	@SerializedName("PreEventCount")
	@Expose
	private String preEventCount;
	@SerializedName("PostEventCount")
	@Expose
	private String postEventCount;
	@SerializedName("StreamReplayBuffer")
	@Expose
	private String streamReplayBuffer;
	@SerializedName("AlarmFrameCount")
	@Expose
	private String alarmFrameCount;
	@SerializedName("SectionLength")
	@Expose
	private String sectionLength;
	@SerializedName("FrameSkip")
	@Expose
	private String frameSkip;
	@SerializedName("MotionFrameSkip")
	@Expose
	private String motionFrameSkip;
	@SerializedName("AnalysisFPS")
	@Expose
	private String analysisFPS;
	@SerializedName("AnalysisUpdateDelay")
	@Expose
	private String analysisUpdateDelay;
	@SerializedName("MaxFPS")
	@Expose
	private String maxFPS;
	@SerializedName("AlarmMaxFPS")
	@Expose
	private String alarmMaxFPS;
	@SerializedName("FPSReportInterval")
	@Expose
	private String fPSReportInterval;
	@SerializedName("RefBlendPerc")
	@Expose
	private String refBlendPerc;
	@SerializedName("AlarmRefBlendPerc")
	@Expose
	private String alarmRefBlendPerc;
	@SerializedName("Controllable")
	@Expose
	private String controllable;
	@SerializedName("ControlId")
	@Expose
	private String controlId;
	@SerializedName("ControlDevice")
	@Expose
	private Object controlDevice;
	@SerializedName("ControlAddress")
	@Expose
	private Object controlAddress;
	@SerializedName("AutoStopTimeout")
	@Expose
	private Object autoStopTimeout;
	@SerializedName("TrackMotion")
	@Expose
	private String trackMotion;
	@SerializedName("TrackDelay")
	@Expose
	private String trackDelay;
	@SerializedName("ReturnLocation")
	@Expose
	private String returnLocation;
	@SerializedName("ReturnDelay")
	@Expose
	private String returnDelay;
	@SerializedName("DefaultView")
	@Expose
	private String defaultView;
	@SerializedName("DefaultRate")
	@Expose
	private String defaultRate;
	@SerializedName("DefaultScale")
	@Expose
	private String defaultScale;
	@SerializedName("SignalCheckColour")
	@Expose
	private String signalCheckColour;
	@SerializedName("WebColour")
	@Expose
	private String webColour;
	@SerializedName("Exif")
	@Expose
	private Boolean exif;
	@SerializedName("Sequence")
	@Expose
	private String sequence;

	
	
	/*public static ZoneMinderMonitorData fromJson(JsonObject jsonObject)
	{
		ZoneMinderMonitorData monitorData = new ZoneMinderMonitorData();
		//JsonObject jsonObject1 = jsonObject.getAsJsonObject("Monitor");
				
		for(Field field  : ZoneMinderMonitorData.class.getDeclaredFields())
		{
		    if (field.isAnnotationPresent(SerializedName.class))
	        {
		    	      //do action
		    	SerializedName sn = (SerializedName)field.getAnnotation(SerializedName.class);
		    	String value = sn.value();
		    	String valueJson = "";
		    	Integer i = 10;
		    	Object curType = null;
		    	try {
		    		
		    		JsonElement element = jsonObject.get(value);
		    		curType  = field.getType();
		    		if (field.getType()==String.class) {
		    			field.set(monitorData, element.getAsString());
		    		}
		    		else if (field.getType()==Boolean.class) {
		    			field.set(monitorData, element.getAsBoolean());
		    			
		    		}
		    		else {
		    			//TODO:: FIX THIS GSON STuff
		    			Integer Test = 1000;
		    		}
		    		

		    	}
		    	catch(Exception ex) {
		    		Integer i2 = 10;
		    	}
		        
		    }
		}
		
		return monitorData;
		
	}
	*/
	
	public String getId()
	{
		return id; 
	}

	
	public String getName()
	{
		return name; 
	}
	
	
	public ZoneMinderMonitorFunctionEnum getFunction()
	{	
		return ZoneMinderMonitorFunctionEnum.getEnum(function);
	}	
	
	public boolean getEnabled()
	{
		return (enabled.equals("1") ? true : false); 
	}

	
	public String getWidth() {return width;}
	
	public String getHeight() {return height;}
	
	public String getColours() {return colours;}
	
	public ZoneMinderMonitorSourceTypeEnum getSourceType()
	{
		return ZoneMinderMonitorSourceTypeEnum.getEnum(type); 
	}


	@Override
	public String getAnalysisFPS() {
		return analysisFPS;
	}


	@Override
	public String getMaxFPS() {
		return maxFPS;
	}


	@Override
	public String getAlarmMaxFPS() {
		return alarmMaxFPS;
	}


	@Override
	public String getLinkedMonitors() {
		return linkedMonitors;
	}


	@Override
	public String getTriggers() {
		return triggers;
	}


	@Override
	public String getFormat() {
		return format;
	}


	@Override
	public boolean getV4LMultiBuffer() {
		return v4LMultiBuffer;
	}


	@Override
	public String getV4LCapturesPerFrame() {
		return v4LCapturesPerFrame;
	}


	@Override
	public String GetColours() {
		return colours;
	}


	@Override
	public String getPalette() {
		return palette;
	}


	@Override
	public String getDeinterlacing() {
		return deinterlacing;
	}


	@Override
	public String getImageBufferCount() {
		return imageBufferCount;
	}


	@Override
	public String getWarmupCount() {
		return warmupCount;
	}


	@Override
	public String getPreEventCount() {
		return preEventCount;
	}


	@Override
	public String getPostEventCount() {
		return postEventCount;
	}


	@Override
	public String getAlarmFrameCount() {

		return alarmFrameCount;
	}

}
