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

import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatus;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatusImpl;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTelnetRequest;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.data.IMonitorDataGeneral;
import name.eskildsen.zoneminder.data.IMonitorDataStillImage;
import name.eskildsen.zoneminder.data.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.data.IZoneMinderEventData;
import name.eskildsen.zoneminder.data.ZoneMinderMonitorDaemonStatus;
import name.eskildsen.zoneminder.event.ZoneMinderEventAction;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;


public interface IZoneMinderMonitor  {

    /** *****************************************************
     * Status methods
      ***************************************************** */
	boolean isConnected();

	IMonitorDataGeneral getMonitorData() throws ZoneMinderInvalidData, ZoneMinderAuthenticationException, ZoneMinderGeneralException, ZoneMinderResponseException;
	ZoneMinderMonitorStatus getMonitorDetailedStatus() throws ZoneMinderInvalidData, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderAuthenticationException;

    IZoneMinderDaemonStatus getCaptureDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;
    IZoneMinderDaemonStatus getAnalysisDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;
    IZoneMinderDaemonStatus getFrameDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;

    
    String getMonitorStreamingPath(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderAuthHashNotEnabled;
    
    IMonitorDataStillImage getMonitorStillImage() throws MalformedURLException, ZoneMinderStreamConfigException;
    IMonitorDataStillImage getMonitorStillImage(Integer scale, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderStreamConfigException;
    IMonitorDataStillImage getMonitorStillImage(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderStreamConfigException;
    
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

	

	/** *****************************************************
     *	Set Methods 
	 * @throws ZoneMinderResponseException 
	 * @throws MalformedURLException 
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderAuthenticationException 
      ***************************************************** */
    ZoneMinderContentResponse SetEnabled(boolean enabled) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	ZoneMinderContentResponse SetFunction(String function) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	ZoneMinderContentResponse SetFunction(ZoneMinderMonitorFunctionEnum function) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderException;
	
}
