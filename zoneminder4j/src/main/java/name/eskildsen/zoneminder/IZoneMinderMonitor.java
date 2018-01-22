package name.eskildsen.zoneminder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorAnalysisDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorCaptureDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorFrameDaemonStatus;
import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatus;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTelnetRequest;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.TelnetAction;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;


public interface IZoneMinderMonitor extends IZoneMinderResponse {

    /** *****************************************************
     * Status methods
      ***************************************************** */
	boolean isConnected();

	IZoneMinderMonitorData getMonitorData();
	ZoneMinderMonitorStatusEnum getMonitorDetailedStatus();

    IZoneMinderDaemonStatus getCaptureDaemonStatus();
    IZoneMinderDaemonStatus getAnalysisDaemonStatus();
    IZoneMinderDaemonStatus getFrameDaemonStatus();

    
    String getMonitorStreamingPath() throws MalformedURLException;
    
    IZoneMinderMonitorImage getMonitorStillImage() throws MalformedURLException;
    IZoneMinderMonitorImage getMonitorStillImage(Integer scale) throws MalformedURLException;
    IZoneMinderMonitorImage getMonitorStillImage(Integer scale, Integer buffer) throws MalformedURLException;
    
    /** *****************************************************
     * Event API
      ***************************************************** */
    IZoneMinderEventData getLastEvent();
    IZoneMinderEventData getEventById(String eventId);
    
    
    /** *****************************************************
     * Event Methods
     * @throws IOException 
     * @throws ZoneMinderUrlNotFoundException 
     * @throws FailedLoginException 
      ***************************************************** */
	void activateForceAlarm(Integer priority, String reason, String note, String showText, Integer timeoutSeconds) throws IOException, FailedLoginException, ZoneMinderUrlNotFoundException;
	void deactivateForceAlarm() throws Exception;

	void SetEnabled(boolean enabled);
	

	/** *****************************************************
     *	Set Methods 
      ***************************************************** */
	void SetFunction(String function);
	void SetFunction(ZoneMinderMonitorFunctionEnum function);
	
}
