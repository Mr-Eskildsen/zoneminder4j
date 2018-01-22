package name.eskildsen.zoneminder;

import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorSourceTypeEnum;

public interface IZoneMinderMonitorData extends  IZoneMinderResponse {
	String getId();
	
	String getName();
	
	
	ZoneMinderMonitorSourceTypeEnum getSourceType();
	
	ZoneMinderMonitorFunctionEnum getFunction();
	
	boolean getEnabled();
	
	String getLinkedMonitors();
	
	String getTriggers();

//private String device;

//private String channel;
	
	String getFormat();
	
	boolean getV4LMultiBuffer();
	
	String getV4LCapturesPerFrame();

	//private String protocol;
	
	//private String method;
	
	//private String host;

	//private String port;

	//private String subPath;
	
	//private String path;
	
	//	private String options;

	//private String user;

	//private String pass;
	
	String getWidth();
	
	String getHeight();
	
	String GetColours();
	
	String getPalette();

	//private String orientation;

	String getDeinterlacing();
	//private Boolean rTSPDescribe;
	
	//private String brightness;
	
	//private String contrast;
	
	//private String hue;

	//private String colour;
	//	private String eventPrefix;
	
	//private String labelFormat;
	
	//private String labelX;
	//private String labelY;


	//private String labelSize;

	String getImageBufferCount();

	String getWarmupCount();
	
	
	String getPreEventCount();
	
	String getPostEventCount();
	//private String streamReplayBuffer;
	String getAlarmFrameCount();
	//private String sectionLength;
	//private String frameSkip;
	//private String motionFrameSkip;
	String getAnalysisFPS();
	//private String analysisUpdateDelay;
	String getMaxFPS();
	String getAlarmMaxFPS();
	//private String fPSReportInterval;
	//private String refBlendPerc;
	//private String alarmRefBlendPerc;
	//private String controllable;
	//private String controlId;
	//private Object controlDevice;
	//private Object controlAddress;
	//private Object autoStopTimeout;
	//private String trackMotion;
	//private String trackDelay;
	//private String returnLocation;
	//private String returnDelay;
	//private String defaultView;
	//private String defaultRate;
	//private String defaultScale;
	//private String signalCheckColour;
	//private String webColour;
	//private Boolean exif;
	//private String sequence;

}
