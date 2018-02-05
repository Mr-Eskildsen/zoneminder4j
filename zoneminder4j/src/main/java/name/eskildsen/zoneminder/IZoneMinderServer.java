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

import name.eskildsen.zoneminder.api.ZoneMinderDiskUsage;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonType;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderHostDaemonStatus;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostLoad;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.exception.http.ZoneMinderResponseException;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;


public interface IZoneMinderServer /*extends IZoneMinderResponse*/ {




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

	//boolean setConfig(ZoneMinderConfigEnum configId, Boolean newValue) throws ZoneMinderGeneralException, ZoneMinderResponseException;
	//boolean setConfig(ZoneMinderConfigEnum configId, String value) throws ZoneMinderGeneralException, ZoneMinderResponseException;

}
