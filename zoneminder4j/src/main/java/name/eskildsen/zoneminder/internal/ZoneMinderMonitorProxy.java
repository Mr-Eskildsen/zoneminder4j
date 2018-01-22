package name.eskildsen.zoneminder.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;

import name.eskildsen.zoneminder.IZoneMinderEventData;
import name.eskildsen.zoneminder.IZoneMinderMonitor;
import name.eskildsen.zoneminder.IZoneMinderMonitorData;
import name.eskildsen.zoneminder.IZoneMinderMonitorImage;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorAnalysisDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorCaptureDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorFrameDaemonStatus;
import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorImage;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatus;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTelnetRequest;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;

public class ZoneMinderMonitorProxy extends ZoneMinderGenericProxy implements IZoneMinderMonitor {


	public ZoneMinderMonitorProxy(IZoneMinderSession session, String monitorId)
	{
		super(session);
		setId( monitorId );
	}
	


    public IZoneMinderDaemonStatus getCaptureDaemonStatus() {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_CAPTURE, ZoneMinderMonitorCaptureDaemonStatus.class);
    }

    public IZoneMinderDaemonStatus getAnalysisDaemonStatus() {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_ANALYSIS, ZoneMinderMonitorAnalysisDaemonStatus.class);
    }

    public IZoneMinderDaemonStatus getFrameDaemonStatus() {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_FRAME, ZoneMinderMonitorFrameDaemonStatus.class);
    }

    
    private  <T> ZoneMinderDaemonStatus getMonitorStatus(String daemonName, Class<T> classType) {

        JsonObject jsonObject = null;
        try {
            String strCommand = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_DAEMONSTATUS_JSON, "MonitorId", getId());
            strCommand = resolveCommands(strCommand, "DaemonName", daemonName);
            jsonObject = getAsJson(strCommand);
	    
        } catch (IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
        	return null;
        }

      //gson.fromJson(jsonObject, classType);
        return (ZoneMinderDaemonStatus) convertToClass(jsonObject, classType);
        		

    }

    protected String buildParameterString(ArrayList<String> params) {
    	return String.join("&", params);
    }
	
	@Override
	public String getMonitorStreamingPath() throws MalformedURLException {
		ArrayList<String> list = new ArrayList<String>();
		
		ZoneMinderSession session =  (ZoneMinderSession)getSession();
		
		list.add("monitor=" + getId());
		list.add("scale=100");
		list.add("buffer=1000");
		
		String paramAuth = "";
		if (session.isAuthenticationHashAllowed()==true)
		{
			list.add("user=" + session.getConnectionInfo().getUserName());
			try {
				list.add("auth=" + session.getAuthHashToken());
				//paramAuth = String.format("&user=%s&auth=%s", session.getConnectionInfo().getUserName(), session.getAuthentificationHash() );
			} catch (ZoneMinderAuthHashNotEnabled e) {
				//Should never appear since we have tested up front
			}
		}
		else if (session.isAuthenticationEnabled())
		{
			list.add("user=" + session.getConnectionInfo().getUserName());
			list.add("pass=" + session.getConnectionInfo().getPassword());
			//paramAuth = String.format("&user=%s&pass=%s", session.getConnectionInfo().getUserName(), session.getConnectionInfo().getPassword()); 
		}
		//String params = "monitor=" + getId() + "&scale=100&buffer=1000" + paramAuth;
		
		//return buildZmsNphURI(params).toString();
		return buildZmsNphURI(buildParameterString(list)).toString();
	}
	
	
	@Override
	public IZoneMinderMonitorImage getMonitorStillImage() throws MalformedURLException {
		return _getMonitorStillImage(null, null);
	}
	
	@Override
	public IZoneMinderMonitorImage getMonitorStillImage(Integer scale) throws MalformedURLException {
		return _getMonitorStillImage(scale, null);
	}

	@Override
	public IZoneMinderMonitorImage getMonitorStillImage(Integer scale, Integer buffer) throws MalformedURLException {
		return _getMonitorStillImage(scale, buffer);
	}
	
	
	public IZoneMinderMonitorImage _getMonitorStillImage(Integer scale, Integer buffer) throws MalformedURLException 
	{
		ArrayList<String> list = new ArrayList<String>();
		
		ZoneMinderSession session =  (ZoneMinderSession)getSession();
		
		list.add("monitor=" + getId());
		list.add("mode=single");
		
		if (scale!=null) {
			list.add("scale=" + scale.toString());
		}
		
		if (buffer!=null) {
			list.add("buffer=1000");
		}
		
		String paramAuth = "";
		if (session.isAuthenticationHashAllowed()==true)
		{
			list.add("user=" + session.getConnectionInfo().getUserName());
			try {
				list.add("auth=" + session.getAuthHashToken());
			} catch (ZoneMinderAuthHashNotEnabled e) {
				//Should never appear since we have tested up front
			}
		}
		else if (session.isAuthenticationEnabled())
		{
			list.add("user=" + session.getConnectionInfo().getUserName());
			list.add("pass=" + session.getConnectionInfo().getPassword());
		}

		URI uri = null;

		uri = buildZmsNphURI(buildParameterString(list));
		
		ByteArrayOutputStream baos = null;
		ZoneMinderMonitorImage imageData = new ZoneMinderMonitorImage(getId());
		try {
			imageData.setHttpUrl(uri.toString());
			
			baos = session.getAsByteArray(uri, true);
			imageData.setImage(baos);
			
		} catch (IOException e) {
			imageData.setImage(null);
		}
		finally {
			
			imageData.setHttpResponseCode(session.getResponseCode());
		}
		
		return imageData;
	}	

	
	
    /** *****************************************************
     * 
     * Event API
     * 
      ***************************************************** */
    public  IZoneMinderEventData getLastEvent() {

        JsonObject jsonObject = null;
        ArrayList<ZoneMinderEvent> list = new ArrayList<ZoneMinderEvent>();
        try {
           
            Integer maxPages = 1;
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {

                jsonObject = getAsJson(
                        resolveCommands(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_MONITOR_JSON, "MonitorId", getId()),
                        resolveCommands(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString()));

                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                
                for (JsonElement curJsonEvent : jsonEvents) {
                	list.add((ZoneMinderEvent)convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class));
                }
            }
            if (list.size()>0) {
            	// Return last event
            	return (IZoneMinderEventData) list.get(list.size() - 1);
            }
            
    	} catch (ArrayIndexOutOfBoundsException | IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
    	
	    	 return null;
	    }
        
        return null;
    }

    
    public  IZoneMinderEventData getEventById_OLD(String eventId) {

        JsonObject jsonObject = null;

        try {
            Integer maxPages = 1;
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {

                jsonObject = getAsJson(
                        resolveCommands(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_MONITOR_JSON, "MonitorId", getId()),
                        resolveCommands(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString()));

                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                
                for (JsonElement curJsonEvent : jsonEvents) {
                	ZoneMinderEvent curEvent = (ZoneMinderEvent) convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
                	if (curEvent != null) {
                	
                        // Return last event
               	 		if (curEvent.getId().equals(eventId))
               	 			return (IZoneMinderEventData)curEvent;
                	}
                }
            }
	     } catch (ArrayIndexOutOfBoundsException | IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
	    	 return null;
	     }

        return null;
    }

    
    public  IZoneMinderEventData getEventById(String eventId) {

        JsonObject jsonObject = null;

        try {
            Integer maxPages = 1;
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {

                jsonObject = getAsJson(
                        resolveCommands(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_EVENT_JSON, "EventId", eventId),
                        resolveCommands(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString()));

                
                //JsonElement element = parser.parse("{\"events\":[{\"Event\":{\"Id\":\"26583\",\"MonitorId\":\"1\",\"Name\":\"New Event\",\"Cause\":\"openHAB\",\"StartTime\":\"2018-01-22 08:09:38\",\"EndTime\":null,\"Width\":\"640\",\"Height\":\"480\",\"Length\":\"0.00\",\"Frames\":null,\"AlarmFrames\":null,\"TotScore\":\"0\",\"AvgScore\":\"0\",\"MaxScore\":\"0\",\"Archived\":\"0\",\"Videoed\":\"0\",\"Uploaded\":\"0\",\"Emailed\":\"0\",\"Messaged\":\"0\",\"Executed\":\"0\",\"Notes\":\"openHAB: openHAB\"},\"thumbData\":\"\"}],\"pagination\":{\"page\":1,\"current\":1,\"count\":1,\"prevPage\":false,\"nextPage\":false,\"pageCount\":1,\"order\":{\"Event.StartTime\":\"asc\",\"Event.MaxScore\":\"asc\"},\"limit\":100,\"options\":{\"order\":{\"Event.StartTime\":\"asc\",\"Event.MaxScore\":\"asc\"}},\"paramType\":\"querystring\"}}");
                //jsonObject = element.getAsJsonObject();

                
                
                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                
                for (JsonElement curJsonEvent : jsonEvents) {
                	ZoneMinderEvent curEvent = (ZoneMinderEvent) convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
                	if (curEvent != null) {
                	
                        // Return last event
               	 		if (curEvent.getId().equals(eventId))
               	 			return (IZoneMinderEventData)curEvent;
                	}
                }
            }
	     } catch (ArrayIndexOutOfBoundsException | IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
	    	 return null;
	     }

        return null;
    }

    
    
    /** *****************************************************
     * 
     * Event Trigger
     * @throws IOException 
     * @throws ZoneMinderUrlNotFoundException 
     * @throws FailedLoginException 
     * 
      ***************************************************** */
	public void activateForceAlarm(Integer priority, String reason,
            String note, String showText, Integer timeoutSeconds) throws IOException, FailedLoginException, ZoneMinderUrlNotFoundException
	{
		ZoneMinderSession session = aquireSession();
	
		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(TelnetAction.ON, getId(), priority, reason,
	            note, showText, timeoutSeconds);
		session.writeTelnet(request.toCommandString());
		
		releaseSession(session);
		
	}

	public void deactivateForceAlarm() throws Exception
	{

		ZoneMinderSession session = aquireSession();

		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(TelnetAction.OFF, getId(), 255, "",
	            "", "", 0);
		session.writeTelnet(request.toCommandString());
		releaseSession(session);
	}


    /** *****************************************************
     * 
     * Monitor
     * @throws Exception 
     * 
      ***************************************************** */
	public void SetEnabled(boolean enabled) {
		String methodPath = "";
		String action = "";
		
		try {
			 methodPath = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
			 if (enabled) {
				 action = "Monitor[Enabled]=1";
			 }
			 else {
				 action = "Monitor[Enabled]=0";
			 }
			
			 sendPost(methodPath, action);
		}
		catch(Exception ex) {
			String s = ex.getMessage();
			s = s;
		}
	
	}
	
	  public IZoneMinderMonitorData getMonitorData() {
		    
		     JsonObject jsonObject = null;
		     try {
		    	 String command = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
		    	 JsonObject obj =  getAsJson(command);
		    	 jsonObject = getAsJson(resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId()))
		    		 			.getAsJsonObject("monitor").getAsJsonObject("Monitor");
		     } catch (IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
		    	 jsonObject = null;
		     }
		     
		     if (jsonObject == null) {
		    	 return null;
		     }
		    
	     	return (IZoneMinderMonitorData)convertToClass(jsonObject, ZoneMinderMonitorData.class);
		    // return (IZoneMinderMonitorData)ZoneMinderMonitorData.fromJson(jsonObject);
	     }
	    
	     
	  public ZoneMinderMonitorStatusEnum getMonitorDetailedStatus() {
	    	 
	         JsonObject jsonObject = null;
	         
             try {
	             String strCommand = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_ALARM_JSON, "MonitorId", getId());
	             strCommand = resolveCommands(strCommand, "MonitorId", getId());
	             jsonObject = getAsJson(strCommand);
	             //TODO:: GSON Fix
	             //ZoneMinderMonitorStatus status = 	gson.fromJson(jsonObject, ZoneMinderMonitorStatus.class);
	             ZoneMinderMonitorStatus status = convertToClass(jsonObject, ZoneMinderMonitorStatus.class);
	             
	             if (status != null) {
	                 return status.getStatus();
	             }
	             
             } catch (FailedLoginException | ZoneMinderUrlNotFoundException | IOException e) {
            	 //Intentionally left blank
             }
	         
	         return ZoneMinderMonitorStatusEnum.UNKNOWN;
	     }
	     
	     
	     public boolean isConnected()
	     {
	    	 boolean _connected =  false;
	    	 ZoneMinderSession session = null;
			try {
				session = aquireSession();
				if (session!=null) {
					_connected = session.isConnectedHttp();
				}

			} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException e) {
				return false;
			}
			finally 
			{
				releaseSession(session);
			}
	    	
	 		
	    	 return _connected;
	     }
	    
	/** *****************************************************
     * 
     * Monitor
     * @throws Exception 
     * 
      ***************************************************** */
	public void SetFunction(String function) {
		String methodPath = "";
		String action = "";
		
		try {
			 methodPath = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
			 action = String.format("Monitor[Function]=%1s", ZoneMinderMonitorFunctionEnum.getEnum(function).toString()); 
			 sendPut(methodPath, action);
		}
		catch(Exception ex) {
			String s = ex.getMessage();
			s = s;
		}
	
	}

	public void SetFunction(ZoneMinderMonitorFunctionEnum function) {
		String methodPath = "";
		String action = "";
		
		try {
			 methodPath = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
			 action = String.format("Monitor[Function]=%1s", function.toString()); 
			 sendPut(methodPath, action);
		}
		catch(Exception ex) {
			String s = ex.getMessage();
			s = s;
		}
	
	}




}
