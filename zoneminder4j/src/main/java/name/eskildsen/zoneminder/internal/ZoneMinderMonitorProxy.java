package name.eskildsen.zoneminder.internal;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.security.auth.login.FailedLoginException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;

import name.eskildsen.zoneminder.IZoneMinderEventData;
import name.eskildsen.zoneminder.IZoneMinderMonitor;
import name.eskildsen.zoneminder.IZoneMinderMonitorData;
import name.eskildsen.zoneminder.IZoneMinderSession;

import name.eskildsen.zoneminder.api.daemon.ZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorAnalysisDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorCaptureDaemonStatus;
import name.eskildsen.zoneminder.api.daemon.ZoneMinderMonitorFrameDaemonStatus;
import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
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

    private  ZoneMinderDaemonStatus getMonitorStatus(String daemonName, Type classType) {

        JsonObject jsonObject = null;
        try {
            String strCommand = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_DAEMONSTATUS_JSON, "MonitorId", getId());
            strCommand = resolveCommands(strCommand, "DaemonName", daemonName);
            jsonObject = getAsJson(strCommand);
	    
        } catch (IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
/*
 * TODO:: FIX THIS        	if (getSession()!=null) {
 
        		getSession().setConnected(false);
        	}
*/	     }

      //gson.fromJson(jsonObject, classType);
        return (ZoneMinderDaemonStatus) convertToClass(jsonObject, classType);
        		

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
                
                //ZoneMinderPagination pagination = gson.fromJson(jsonObject.getAsJsonObject("pagination"), ZoneMinderPagination.class);
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt(); //pagination.getPageCount();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                
                for (JsonElement curJsonEvent : jsonEvents) {

               	 //list.add(gson.fromJson(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class));

                	list.add((ZoneMinderEvent)convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class));
       	 
                }
            }
	     } catch (IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
/*
 * 	    	 
 //TODO:: FIX THIS
	        	if (getSessionManager()!=null) {
	        		getSessionManager().setConnected(false);
	        	}
*/	        	
	    	 return null;
	     }

        // Return last event
        return (IZoneMinderEventData) list.get(list.size() - 1);
    }

    
    public  IZoneMinderEventData getEventById(String eventId) {

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
               	 //ZoneMinderEvent curEvent = gson.fromJson(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
                	ZoneMinderEvent curEvent = (ZoneMinderEvent) convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
               	 if (curEvent.getId().equals(eventId))
               		 return (IZoneMinderEventData)curEvent;
                }
            }
	     } catch (IOException | FailedLoginException | ZoneMinderUrlNotFoundException e) {
/*
 //TODO:: FIX THIS
	    	 if (getSessionManager()!=null) {
	        		getSessionManager().setConnected(false);
	        	}
*/
	    	 }

        // Return last event
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
		    	 
		    	 /*
//TODO::FIX THIS
		        	if (getSessionManager()!=null) {
		        		getSessionManager().setConnected(false);
		        	}
		    	  */
		     }
		     
		     if (jsonObject == null) {
		    	 return null;
		     }
		    
	     	return (IZoneMinderMonitorData)convertToClass(jsonObject, ZoneMinderMonitorData.class);
	     			
//	     			gson.fromJson(jsonObject, ZoneMinderMonitorData.class);
	    
	     }
	    
	     
	  public ZoneMinderMonitorStatusEnum getMonitorDetailedStatus() {
	    	 
	         JsonObject jsonObject = null;
	         
             try {
	             String strCommand = resolveCommands(ZoneMinderServerConstants.SUBPATH_API_MONITOR_ALARM_JSON, "MonitorId", getId());
	             strCommand = resolveCommands(strCommand, "MonitorId", getId());
	             jsonObject = getAsJson(strCommand);
	             ZoneMinderMonitorStatus status = gson.fromJson(jsonObject, ZoneMinderMonitorStatus.class); 
	             
	             return status.getStatus();
             } catch (FailedLoginException | ZoneMinderUrlNotFoundException | IOException e) {
            	 //INtentionally left blank
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
