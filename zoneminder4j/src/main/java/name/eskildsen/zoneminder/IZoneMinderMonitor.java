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


import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorDaemonStatus;
import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatus;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTelnetRequest;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.event.ZoneMinderEventAction;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;


public interface IZoneMinderMonitor /*extends IZoneMinderResponse*/ {

    /** *****************************************************
     * Status methods
      ***************************************************** */
	boolean isConnected();

	IMonitorDataGeneral getMonitorData() throws ZoneMinderInvalidData;
	ZoneMinderMonitorStatusEnum getMonitorDetailedStatus();

    IZoneMinderDaemonStatus getCaptureDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;
    IZoneMinderDaemonStatus getAnalysisDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;
    IZoneMinderDaemonStatus getFrameDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;

    
    String getMonitorStreamingPath() throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException;
    
    IMonitorDataStillImage getMonitorStillImage() throws MalformedURLException, ZoneMinderStreamConfigException;
    IMonitorDataStillImage getMonitorStillImage(Integer scale) throws MalformedURLException, ZoneMinderStreamConfigException;
    IMonitorDataStillImage getMonitorStillImage(Integer scale, Integer buffer) throws MalformedURLException, ZoneMinderStreamConfigException;
    
    /** *****************************************************
     * Event API
      ***************************************************** */
    IZoneMinderEventData getLastEvent();
    IZoneMinderEventData getEventById(String eventId);
    
    
    /** *****************************************************
     * Event Methods
     * @throws ZoneMinderResponseException 
     * @throws MalformedURLException 
     * @throws ZoneMinderGeneralException 
     * @throws ZoneMinderAuthenticationException 
     * @throws ZoneMinderException 
     * @throws IOException 
     * @throws ZoneMinderUrlNotFoundException 
     * @throws FailedLoginException 
      ***************************************************** */
//	void activateForceAlarm(Integer priority, String reason, String note, String showText, Integer timeoutSeconds) throws IOException, FailedLoginException, ZoneMinderUrlNotFoundException;
//	void deactivateForceAlarm() throws Exception;

    ZoneMinderContentResponse SetEnabled(boolean enabled) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	

	/** *****************************************************
     *	Set Methods 
	 * @throws ZoneMinderResponseException 
	 * @throws MalformedURLException 
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderAuthenticationException 
      ***************************************************** */
	ZoneMinderContentResponse SetFunction(String function) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	ZoneMinderContentResponse SetFunction(ZoneMinderMonitorFunctionEnum function) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	
}
