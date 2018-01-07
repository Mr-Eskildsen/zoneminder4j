package name.eskildsen.zoneminder;

import java.io.IOException;
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
import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonType;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderHostDaemonStatus;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostLoad;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.internal.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.internal.ZoneMinderSession;

public interface IZoneMinderServer extends IZoneMinderResponse {




	/* ******************************************************
	 * 
	 * Status calls
	 * 
	 ***************************************************** */
	boolean isConnected();
	boolean isApiEnabled();
	boolean isTriggerOptionEnabled();

	/* ******************************************************
	 * 
	 * Host API
	 * 
	 ***************************************************** */
	IZoneMinderHostVersion getHostVersion() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException; 
	IZoneMinderHostLoad getHostCpuLoad() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException; 

	IZoneMinderDaemonStatus getHostDaemonCheckState();
	IZoneMinderDiskUsage getHostDiskUsage() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException;


	/** *****************************************************
	 * 
	 * Monitor API
	 * 
	 ***************************************************** */

	ArrayList<IZoneMinderMonitorData> getMonitors();


	/** *****************************************************
	 * 
	 * Config API
	 * 
	 ***************************************************** */    
	ZoneMinderConfig getConfig(ZoneMinderConfigEnum configId);

	boolean setConfig(ZoneMinderConfigEnum configId, Boolean newValue);

	boolean setConfig(ZoneMinderConfigEnum configId, String value);

}
