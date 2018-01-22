package name.eskildsen.zoneminder.internal;


import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.IZoneMinderDiskUsage;
import name.eskildsen.zoneminder.IZoneMinderHostLoad;
import name.eskildsen.zoneminder.IZoneMinderHostVersion;
import name.eskildsen.zoneminder.IZoneMinderMonitorData;
import name.eskildsen.zoneminder.IZoneMinderServer;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.ZoneMinderDiskUsage;
import name.eskildsen.zoneminder.api.ZoneMinderResponseData;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonType;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderHostDaemonStatus;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostLoad;
import name.eskildsen.zoneminder.api.host.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.trigger.ZoneMinderEventNotifier;

public class ZoneMinderServerProxy  extends ZoneMinderGenericProxy implements IZoneMinderServer {

	public ZoneMinderServerProxy(IZoneMinderSession session) {
		super(session);
	}


	public boolean isConnected()
	{
		boolean _connected =  false;
		ZoneMinderSession session = null;
		try {
			session = aquireSession();
		} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException e) {
			return false;
		}
		_connected = session.isConnectedHttp();
		releaseSession(session);
		return _connected;

	}



	@Override
	public boolean isApiEnabled() {
		try {
			ZoneMinderConfig config = getConfig(ZoneMinderConfigEnum.ZM_OPT_USE_API);
			return (config.getValueAsString().equals("1") ? true : false);
		}
		catch(Exception ex) {
			return false;
		}
	}


	@Override
	public boolean isTriggerOptionEnabled() {
		try {
			ZoneMinderConfig config = getConfig(ZoneMinderConfigEnum.ZM_OPT_TRIGGERS);
			return (config.getValueAsString().equals("1") ? true : false);
		}
		catch(Exception ex) {

		}
		return true;
	}


	

	/** *****************************************************
	 * 
	 * Shared API helpers
	 * @throws IOException 
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws FailedLoginException 
	 * 
	 ***************************************************** */

	protected IZoneMinderDiskUsage getDiskUsage(String id) throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException {

		JsonObject jsonObject = null;

		jsonObject = getAsJson(ZoneMinderServerConstants.SUBPATH_API_HOST_DISKPERCENT_JSON).get("usage")
				.getAsJsonObject();

		Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (entry.getKey().equalsIgnoreCase(id)) {
				JsonObject object = (JsonObject) entry.getValue();
				return (IZoneMinderDiskUsage)convertToClass(object, ZoneMinderDiskUsage.class);
			}
		}

		//Just return response codes from call
		return (IZoneMinderDiskUsage)convertToClass(null, ZoneMinderDiskUsage.class);

	}
	
	
	
	/*******************************************************
	 * 
	 * Host API
	 * 
	 ***************************************************** */

	/**
	 * Represents the API call to api/getVersion.json 
	 *
	 * 
	 * @return Return object of type {@see ZoneMinderHostVersion} 
	 * @throws IOException 
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws FailedLoginException 
	 */
	public IZoneMinderHostVersion getHostVersion() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException 
	{
		JsonObject jsonObject = null;
		jsonObject = getAsJson(ZoneMinderServerConstants.SUBPATH_API_HOST_VERSION_JSON);
		return (IZoneMinderHostVersion)convertToClass(jsonObject, ZoneMinderHostVersion.class);


	}


	/**
	 * Represents the API call to api/host/getLoad.json 
	 *
	 * 
	 * @return Return object of type {@see ZoneMinderHostCpuLoad} 
	 * @throws IOException 
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws FailedLoginException 
	 */
	public IZoneMinderHostLoad getHostCpuLoad() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException 
	{
		JsonObject jsonObject = null;

		jsonObject = getAsJson(ZoneMinderServerConstants.SUBPATH_API_HOST_CPULOAD_JSON);
		return (IZoneMinderHostLoad)convertToClass(jsonObject, ZoneMinderHostLoad.class);
	}

	public  IZoneMinderDaemonStatus getHostDaemonCheckState()  {

		ZoneMinderHostDaemonStatus daemonState = null;
		try {
			JsonObject jsonObject = getAsJson(ZoneMinderServerConstants.SUBPATH_API_HOST_DAEMON_CHECKSTATE);
			//Integer curState = getAsJson(ZoneMinderServerConstants.SUBPATH_API_HOST_DAEMON_CHECKSTATE).get("result").getAsInt();
			/* TODO:: GSON CRAPx½
			daemonState = (ZoneMinderHostDaemonStatus) ZoneMinderDaemonStatus.Create(ZoneMinderDaemonType.DAEMON_HOST, curState, null);
			daemonState.setHttpResponseCode(getHttpResponseCode());
			daemonState.setHttpResponseMessage(getHttpResponseMessage());
			*/
			daemonState = ZoneMinderResponseData.createFromJson(jsonObject, ZoneMinderHostDaemonStatus.class);
			daemonState.setHttpResponseCode(getHttpResponseCode());
			daemonState.setHttpResponseMessage(getHttpResponseMessage());

		}
		catch (FailedLoginException | ZoneMinderUrlNotFoundException | IOException e) {
		}


		return (IZoneMinderDaemonStatus)daemonState;
	}

	public IZoneMinderDiskUsage getHostDiskUsage() throws FailedLoginException, ZoneMinderUrlNotFoundException, IOException {

		return getDiskUsage("Total");
	}



	/** *****************************************************
	 * 
	 * Monitor API
	 ***************************************************** */

	public ArrayList<IZoneMinderMonitorData> getMonitors()  {

		JsonObject jsonObject = null;
		try {
			jsonObject = getAsJson(ZoneMinderServerConstants.SUBPATH_API_MONITORS_JSON);
		}
		catch(FailedLoginException e) {
			jsonObject = null;
		} catch(ZoneMinderUrlNotFoundException e) {
			jsonObject = null;	
		} catch(IOException e)
		{
			jsonObject = null;
		}


		if (jsonObject == null) {
			return null;
		}


		JsonArray arrMonitorJson = jsonObject.getAsJsonArray("monitors");
		ArrayList<IZoneMinderMonitorData> arrMonitor = new ArrayList<IZoneMinderMonitorData>();
		for (JsonElement cur : arrMonitorJson) {


			ZoneMinderMonitorData monitorData = ZoneMinderResponseData.createFromJson(((JsonObject)cur).getAsJsonObject("Monitor"), ZoneMinderMonitorData.class);
			arrMonitor.add(monitorData);
			//arrMonitor.add(gson.fromJson(((JsonObject)cur).getAsJsonObject("Monitor"), ZoneMinderMonitorData.class));
			//arrMonitor.add(ZoneMinderMonitorData.fromJson(((JsonObject)cur).getAsJsonObject("Monitor")));
			
		}
		return arrMonitor;
	}





	


}
