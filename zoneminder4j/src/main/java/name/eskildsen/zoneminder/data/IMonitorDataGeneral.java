package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorSourceTypeEnum;

public interface IMonitorDataGeneral extends  IZoneMinderResponse {
	String getId();
	
	String getName();
	
	ZoneMinderMonitorSourceTypeEnum getSourceType();
	
	ZoneMinderMonitorFunctionEnum getFunction();
	
	boolean getEnabled();
	
	String getLinkedMonitors();
	
	String getTriggers();
	
	String getFormat();
	
	boolean getV4LMultiBuffer();
	
	String getV4LCapturesPerFrame();
		
	String getWidth();
	
	String getHeight();
	
	String GetColours();
	
	String getPalette();


	String getDeinterlacing();

	String getImageBufferCount();

	String getWarmupCount();
	
	
	String getPreEventCount();
	
	String getPostEventCount();

	String getAlarmFrameCount();

	String getAnalysisFPS();

	String getMaxFPS();

	String getAlarmMaxFPS();
	
}
