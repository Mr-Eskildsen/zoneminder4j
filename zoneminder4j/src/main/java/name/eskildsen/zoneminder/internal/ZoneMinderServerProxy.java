package name.eskildsen.zoneminder.internal;


import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderServer;

import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.common.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.data.IMonitorDataGeneral;
import name.eskildsen.zoneminder.data.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.data.IZoneMinderDiskUsage;
import name.eskildsen.zoneminder.data.IZoneMinderHostLoad;
import name.eskildsen.zoneminder.data.IZoneMinderHostVersion;
import name.eskildsen.zoneminder.data.ZoneMinderConfig;
import name.eskildsen.zoneminder.data.ZoneMinderConfigImpl;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;
import name.eskildsen.zoneminder.data.ZoneMinderDiskUsage;
import name.eskildsen.zoneminder.data.ZoneMinderHostDaemonStatus;
import name.eskildsen.zoneminder.data.ZoneMinderHostLoad;
import name.eskildsen.zoneminder.data.ZoneMinderHostVersion;
import name.eskildsen.zoneminder.data.ZoneMinderMonitorDaemonStatus;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public class ZoneMinderServerProxy  extends ZoneMinderGenericProxy implements IZoneMinderServer {
	
	public ZoneMinderServerProxy(IZoneMinderConnectionHandler connection) {
		super(connection);
	}


	public boolean isConnected()
	{
		if (getConnection()==null)
			return false;
		
		return getConnection().isConnected();

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

	@Override
	public ZoneMinderConfig getConfig(ZoneMinderConfigEnum configEnum) throws ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderResponseException, ZoneMinderInvalidData {
		ZoneMinderContentResponse response = null;
		//TODO Hardcoded value
        response = getConnection().getPageContent(buildUriApi( replaceParameter(ZoneMinderServerConstants.SUBPATH_API_SERVER_GET_CONFIG_JSON, "ConfigId", configEnum.name())));
        return (ZoneMinderConfig)IZoneMinderResponse.createFromJson(response.getContentAsJsonObject().getAsJsonObject("config").getAsJsonObject("Config"),response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderConfigImpl.class);

	}

	/** *****************************************************
	 * 
	 * Shared API helpers
	 * @throws IOException 
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws FailedLoginException 
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderResponseException 
	 * @throws ZoneMinderInvalidData 
	 * @throws ZoneMinderAuthenticationException 
	 * 
	 ***************************************************** */

	protected IZoneMinderDiskUsage getDiskUsage(String id) throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {

		JsonObject jsonObject = null;

		ZoneMinderContentResponse response = null;
		//TODO Hardcoded value
        response = getConnection().getPageContent(buildUriApi( ZoneMinderServerConstants.SUBPATH_API_HOST_DISKPERCENT_JSON ));
      //TODO Hardcoded value
        jsonObject = response.getContentAsJsonObject().get("usage").getAsJsonObject();
       
		
		Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (entry.getKey().equalsIgnoreCase(id)) {
				JsonObject object = (JsonObject) entry.getValue();
				return IZoneMinderResponse.createFromJson(jsonObject, response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderDiskUsage.class);
			}
		}

		//Just return response codes from call
		return IZoneMinderResponse.createFromJson(null, response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderDiskUsage.class);
		//return (IZoneMinderDiskUsage)convertToClass(null, ZoneMinderDiskUsage.class);

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
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderResponseException 
	 * @throws ZoneMinderInvalidData 
	 * @throws ZoneMinderAuthenticationException 
	 */
	public IZoneMinderHostVersion getHostVersion() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException 
	{
		ZoneMinderContentResponse response = null;
        response = getConnection().getPageContent(buildUriApi( ZoneMinderServerConstants.SUBPATH_API_HOST_VERSION_JSON));
        return IZoneMinderResponse.createFromJson(response.getContentAsJsonObject(),response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderHostVersion.class);
	}


	/**
	 * Represents the API call to api/host/getLoad.json 
	 *
	 * 
	 * @return Return object of type {@see ZoneMinderHostCpuLoad} 
	 * @throws IOException 
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderResponseException 
	 * @throws ZoneMinderInvalidData 
	 * @throws ZoneMinderAuthenticationException 
	 */
	public IZoneMinderHostLoad getHostCpuLoad() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException 
	{
		ZoneMinderContentResponse response = null;
        response = getConnection().getPageContent(buildUriApi( ZoneMinderServerConstants.SUBPATH_API_HOST_CPULOAD_JSON ));
        return IZoneMinderResponse.createFromJson(response.getContentAsJsonObject(),response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderHostLoad.class);
	}

	public  boolean isDaemonRunning()  { 

		try {
			IZoneMinderDaemonStatus daemonStatus = getHostDaemonCheckState();
			if (daemonStatus!=null) {
				return daemonStatus.getStatus();
			}
		} catch (ZoneMinderGeneralException | ZoneMinderResponseException | ZoneMinderInvalidData
				| ZoneMinderAuthenticationException e) {
			//Intentionally left blank
		}
		return false;
	}
		
	public  IZoneMinderDaemonStatus getHostDaemonCheckState() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException  {
        ZoneMinderContentResponse response = null;
        try {
        	response = getConnection().getPageContent(buildUriApi( ZoneMinderServerConstants.SUBPATH_API_HOST_DAEMON_CHECKSTATE ));
            return IZoneMinderResponse.createFromJson(response.getContentAsJsonObject(),response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderHostDaemonStatus.class);
            
        } catch (IOException e) {
        	return null;
        }
	}

	public IZoneMinderDiskUsage getHostDiskUsage() throws ZoneMinderUrlNotFoundException, IOException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {

		return getDiskUsage("Total");
	}



	/** *****************************************************
	 * 
	 * Monitor API
	 * @throws ZoneMinderGeneralException 
	 * @throws ZoneMinderResponseException 
	 * @throws ZoneMinderInvalidData 
	 * @throws ZoneMinderAuthenticationException 
	 ***************************************************** */

	public ArrayList<IMonitorDataGeneral> getMonitors() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException  {
        ZoneMinderContentResponse response = null;
        ArrayList<IMonitorDataGeneral> arrMonitor = new ArrayList<IMonitorDataGeneral>();
		try {
        	response = getConnection().getPageContent(buildUriApi( ZoneMinderServerConstants.SUBPATH_API_MONITORS_JSON ));
        	JsonArray arrMonitorJson = response.getContentAsJsonObject().getAsJsonArray("monitors");	
        	for (JsonElement cur : arrMonitorJson) {
    			//TODO Fix Hardcoded Object Id
    			ZoneMinderMonitorData monitorData = IZoneMinderResponse.createFromJson(((JsonObject)cur).getAsJsonObject("Monitor"), response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderMonitorData.class);
    			arrMonitor.add(monitorData);
    		}
        } catch (IOException e) {
        	return null;
        }
		return arrMonitor;
	}


}
