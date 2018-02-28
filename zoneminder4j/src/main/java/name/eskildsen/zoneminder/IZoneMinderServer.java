package name.eskildsen.zoneminder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.common.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.common.ZoneMinderDaemonType;
import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.data.IMonitorDataGeneral;
import name.eskildsen.zoneminder.data.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.data.IZoneMinderDiskUsage;
import name.eskildsen.zoneminder.data.IZoneMinderHostLoad;
import name.eskildsen.zoneminder.data.IZoneMinderHostVersion;
import name.eskildsen.zoneminder.data.ZoneMinderConfig;
import name.eskildsen.zoneminder.data.ZoneMinderConfigImpl;
import name.eskildsen.zoneminder.data.ZoneMinderDiskUsage;
import name.eskildsen.zoneminder.data.ZoneMinderHostDaemonStatus;
import name.eskildsen.zoneminder.data.ZoneMinderHostLoad;
import name.eskildsen.zoneminder.data.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;


public interface IZoneMinderServer {




	/* ******************************************************
	 * 
	 * Status calls
	 * 
	 ***************************************************** */
	boolean isConnected();
	boolean isDaemonRunning();
	boolean isApiEnabled();

	/* ******************************************************
	 * 
	 * Call to query server settings 
	 * 
	 ***************************************************** */
	boolean isTriggerOptionEnabled();
	
	
	

	/* ******************************************************
	 * 
	 * Host API
	 * 
	 ***************************************************** */
	IZoneMinderHostVersion getHostVersion() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException; 
	IZoneMinderHostLoad getHostCpuLoad() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException; 

	IZoneMinderDaemonStatus getHostDaemonCheckState() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;
	IZoneMinderDiskUsage getHostDiskUsage() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;


	/** *****************************************************
	 * 
	 * Monitor API
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderResponseException 
	 * @throws ZoneMinderInvalidData 
	 * @throws ZoneMinderAuthenticationException 
	 * 
	 ***************************************************** */

	ArrayList<IMonitorDataGeneral> getMonitors() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException;


	/** *****************************************************
	 * 
	 * Config API
	 * @throws ZoneMinderInvalidData 
	 * @throws MalformedURLException 
	 * @throws ZoneMinderAuthenticationException 
	 * 
	 ***************************************************** */    
	ZoneMinderConfig getConfig(ZoneMinderConfigEnum configId) throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderAuthenticationException, MalformedURLException, ZoneMinderInvalidData;


}
