package name.eskildsen.zoneminder.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.UrlEncoded;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import name.eskildsen.zoneminder.IZoneMinderMonitor;
import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderHttpSession;
import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorData;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorImage;
import name.eskildsen.zoneminder.data.IMonitorDataGeneral;
import name.eskildsen.zoneminder.data.IMonitorDataStillImage;
import name.eskildsen.zoneminder.data.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.data.IZoneMinderEventData;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;
import name.eskildsen.zoneminder.data.ZoneMinderMonitorDaemonStatus;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthHashNotEnabled;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.exception.ZoneMinderStreamConfigException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.jetty.JettyQueryParameter;
import name.eskildsen.zoneminder.api.monitor.ZoneMinderMonitorStatus;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorFunctionEnum;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;


public class ZoneMinderMonitorProxy extends ZoneMinderGenericProxy implements IZoneMinderMonitor {


	public ZoneMinderMonitorProxy(IZoneMinderConnectionHandler connection, String monitorId)
	{
		super(connection);
		setId( monitorId );
	}
	


    public IZoneMinderDaemonStatus getCaptureDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_CAPTURE, ZoneMinderMonitorDaemonStatus.class);
    }

    public IZoneMinderDaemonStatus getAnalysisDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_ANALYSIS, ZoneMinderMonitorDaemonStatus.class);
    }

    public IZoneMinderDaemonStatus getFrameDaemonStatus() throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {
        return (IZoneMinderDaemonStatus)getMonitorStatus( DAEMON_NAME_FRAME, ZoneMinderMonitorDaemonStatus.class);
    }

    
    private  <T> IZoneMinderDaemonStatus getMonitorStatus(String daemonName, Class<T> classType) throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData, ZoneMinderAuthenticationException {

        JsonObject jsonObject = null;
        ZoneMinderContentResponse response = null;
        try {
        	//TODO HArdcoded values
            String path = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_MONITOR_DAEMONSTATUS_JSON, "MonitorId", getId());
            path = replaceParameter(path, "DaemonName", daemonName);
            
            response = getConnection().getPageContent(buildUriApi( path ));
            return IZoneMinderResponse.createFromJson(response.getContentAsJsonObject(), response.getHttpStatus(), response.getHttpResponseMessage(), response.getHttpRequestURI(), ZoneMinderMonitorDaemonStatus.class);
            
        } catch (IOException e) {
        	return null;
        }
        		

    }

    protected String buildParameterString(ArrayList<String> params) {
    	return String.join("&", params);
    }
	
    
    protected ArrayList<JettyQueryParameter> createImageParameterList(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws ZoneMinderAuthHashNotEnabled
    {
    	ArrayList<JettyQueryParameter> list = new ArrayList<JettyQueryParameter>();
		list.add(new JettyQueryParameter("monitor", getId()));
		list.add(new JettyQueryParameter("scale", scale.toString()));
		list.add(new JettyQueryParameter("buffer", buffer.toString()));
		
		if (extraParams!=null) {
			//Add custom parameters to query
			for (int idx = 0;idx<extraParams.length;idx++) {
				list.add(extraParams[idx]);	
			}
		}
		
		String paramAuth = "";
		if (getConnection().getAuthenticationHashAllowed() && getConnection().isAuthenticationEnabled()) {
			
			switch(getConnection().getAuthenticationHashReleayMethod()) {
			case Plain:

				list.add(new JettyQueryParameter("pass", getConnection().getStreamingPassword()));
				//list.add(new JettyQueryParameter("pass", getConnection().getPassword()));
	
			case None:
				list.add(new JettyQueryParameter("user", getConnection().getStreamingUserName()));
				break;
				
			case Hashed:
				list.add(new JettyQueryParameter("auth", getConnection().getAuthHashToken()));
				break;
			}
		}

    	return list;
    }

    /*
    protected List<JettyQueryParameter> getMonitorStreamingParameterList(){
    	List<JettyQueryParameter> list = new ArrayList<JettyQueryParameter>();

		//TODO Fix hardcoded
		list.add(new JettyQueryParameter("monitor", getId()));
		list.add(new JettyQueryParameter("scale", "100"));
		list.add(new JettyQueryParameter("buffer", "1000"));
		

		String paramAuth = "";
		if (getConnection().isAuthenticationHashAllowed()==true)
		{
			list.add(new JettyQueryParameter("user", getConnection().getUserName()));
			try {
				list.add(new JettyQueryParameter("auth", getConnection().getAuthHashToken()));
			} catch (ZoneMinderAuthHashNotEnabled e) {
				//Should never appear since we have tested up front
			}
		}
		else if (getConnection().isAuthenticationEnabled())
		{
			list.add(new JettyQueryParameter("user", getConnection().getUserName()));
			list.add(new JettyQueryParameter("pass", getConnection().getPassword()));
		}
		return list; 
		
    }
    */
    
	protected URI getMonitorStreamingURI() throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException {
		return buildUriZmsNph("");
	}
    
    @Override
	public String getMonitorStreamingPath(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderAuthHashNotEnabled {
		ZoneMinderContentResponse zmcr = null;
	//TODO HAndle Scale + Buffer		
			
		List<JettyQueryParameter> parameters = createImageParameterList(scale, buffer, extraParams);
		//parameters.add(new JettyQueryParameter("mode", "mjpeg");


		UriBuilder uriBuilderStreaming = UriBuilder.fromUri(getMonitorStreamingURI());
		
		//String query = "";
		//String prefix = "";
		for (int idx=0;idx<parameters.size();idx++) {
			JettyQueryParameter param = parameters.get(idx);
			uriBuilderStreaming.queryParam(param.getName(), param.getValue());
			
			//query += prefix + queryParam.getName() + "=" + URLEncoder.encode(queryParam.getValue(), "UTF-8");
			//prefix = "&";
		}

			
		return uriBuilderStreaming.build().toString();
		
	}

    
	@Override
	public IMonitorDataStillImage getMonitorStillImage() throws MalformedURLException, ZoneMinderStreamConfigException {
		return _getMonitorStillImage(null, null, null);
	}
	
	@Override
	public IMonitorDataStillImage getMonitorStillImage(Integer scale, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderStreamConfigException {
		return _getMonitorStillImage(scale, null, extraParams);
	}

	@Override
	public IMonitorDataStillImage getMonitorStillImage(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderStreamConfigException {
		return _getMonitorStillImage(scale, buffer, extraParams);
	}
	
	
	public IMonitorDataStillImage _getMonitorStillImage(Integer scale, Integer buffer, JettyQueryParameter[] extraParams) throws MalformedURLException, ZoneMinderStreamConfigException 
	{
		ZoneMinderMonitorImage imageData = null; 
		ZoneMinderContentResponse zmcr = null;
		try {
	//TODO HAndle Scale + Buffer		
			URI uri = getMonitorStreamingURI();
			List<JettyQueryParameter> parameters = createImageParameterList(scale, buffer, extraParams);
			parameters.add(new JettyQueryParameter("mode", "single"));
	
			ByteArrayOutputStream baos = null;
			try {

				zmcr = getConnection().getPageContent(uri, parameters);
				baos = zmcr.getContentAsByteArray();
				
				imageData = new ZoneMinderMonitorImage(getId(), zmcr.getHttpStatus(), zmcr.getHttpResponseMessage(), zmcr.getHttpRequestURI());
				imageData.setImage(baos);
				
			}
			catch(IOException ioe) {
				//TODO Handle IOException
			}
			finally {
				if (imageData!=null) {
					imageData.setResponseStatus(zmcr.getHttpStatus());
				}
			}
			//TODO Clean this				
//		} catch (ZoneMinderStreamConfigException sce) {
			//		throw sce;
		} catch(Exception ex) {
			return null;
		}
		finally {
			
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
        ZoneMinderContentResponse response = null;
        ArrayList<ZoneMinderEvent> list = new ArrayList<ZoneMinderEvent>();
        try {
           
            Integer maxPages = 1;
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {
            	
	        	//TODO HArdcoded values
	            String path = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_MONITOR_JSON, "MonitorId", getId());
                
              	//Quite Ugly
        		//TODO Fix hardcoded
            	response = getConnection().getPageContent(buildUriApi(path), createQueryParameterListFromString(replaceParameter(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString())));
                        
                jsonObject = response.getContentAsJsonObject();
            	
                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                ZoneMinderEvent event = null;
                for (JsonElement curJsonEvent : jsonEvents) {
                	
                	event = IZoneMinderResponse.createFromJson(((JsonObject)curJsonEvent).getAsJsonObject("Event"),
                											response.getHttpStatus(), 
                											response.getHttpResponseMessage(), 
                											response.getHttpRequestURI(), 
                											ZoneMinderEvent.class);
                	list.add(event);
                }
            }
            if (list.size()>0) {
            	// Return last event
            	return (IZoneMinderEventData) list.get(list.size() - 1);
            }
            
    	} catch (Exception e) {
    	
	    	 return null;
	    }
        
        return null;
    }

    /*
    private  IZoneMinderEventData getEventById_OLD(String eventId) throws ZoneMinderGeneralException, ZoneMinderResponseException, ZoneMinderInvalidData {

        JsonObject jsonObject = null;

        try {
            Integer maxPages = 1;
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {

                jsonObject = getAsJson(
                        resolveCommands(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_MONITOR_JSON, "MonitorId", getId()),
                        //TODO QUite qugly
                        createQueryParameterListFromString(resolveCommands(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString())));

                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                
                for (JsonElement curJsonEvent : jsonEvents) {
                	//ZoneMinderEvent curEvent = (ZoneMinderEvent) convertToClass(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
                	ZoneMinderEvent curEvent = ZoneMinderCoreData.createFromJson(((JsonObject)curJsonEvent).getAsJsonObject("Event"), ZoneMinderEvent.class);
                			
                	if (curEvent != null) {
                	
                        // Return last event
               	 		if (curEvent.getId().equals(eventId))
               	 			return (IZoneMinderEventData)curEvent;
                	}
                }
            }
	     } catch (ArrayIndexOutOfBoundsException | IOException |  ZoneMinderUrlNotFoundException e) {
	    	 return null;
	     }

        return null;
    }

    */
    public  IZoneMinderEventData getEventById(String eventId) {

        JsonObject jsonObject = null;

        try {
            ZoneMinderContentResponse response = null;
            Integer maxPages = 1;
            //TODO Should not be neede with page loop
            for (Integer curPageIdx = 1; curPageIdx <= maxPages; curPageIdx++) {

            	
              	//TODO HArdcoded values
	            String path = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_EVENTS_SPECIFIC_EVENT_JSON, "EventId", eventId);
                
              	//Quite Ugly
        		//TODO Fix hardcoded
            	response = getConnection().getPageContent(buildUriApi(path), createQueryParameterListFromString(replaceParameter(QUERY_CURRENTPAGE, "currentPage", curPageIdx.toString())));
                
                jsonObject = response.getContentAsJsonObject();
                
                if (jsonObject == null) {
                    return null;
                }

                JsonElement elemPageCount = jsonObject.getAsJsonObject("pagination").get("pageCount");
                
                if (curPageIdx == 1) {
                    maxPages = elemPageCount.getAsInt();
                }
                
                JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
                ZoneMinderEvent curEvent = null;
                for (JsonElement curJsonEvent : jsonEvents) {
                	curEvent = IZoneMinderResponse.createFromJson(((JsonObject)curJsonEvent).getAsJsonObject("Event"),
                						response.getHttpStatus(), 
										response.getHttpResponseMessage(), 
										response.getHttpRequestURI(), 
										ZoneMinderEvent.class);

                	if (curEvent != null) {
                        // Return last event
               	 		if (curEvent.getId().equals(eventId))
               	 			return (IZoneMinderEventData)curEvent;
                	}
                }
            }
	     } catch (Exception e) {
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
	/*public void activateForceAlarm(Integer priority, String reason,
            String note, String showText, Integer timeoutSeconds) throws IOException, FailedLoginException, ZoneMinderUrlNotFoundException
	{
		IZoneMinderHttpSessionInternal session = aquireSession();
	
		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(TelnetAction.ON, getId(), priority, reason,
	            note, showText, timeoutSeconds);
		session.writeTelnet(request.toCommandString());
		
		releaseSession(session);
		
	}

	public void deactivateForceAlarm() throws Exception
	{

		IZoneMinderHttpSessionInternal session = aquireSession();

		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(TelnetAction.OFF, getId(), 255, "",
	            "", "", 0);
		session.writeTelnet(request.toCommandString());
		releaseSession(session);
	}
*/

    /** *****************************************************
     * 
     * Monitor
     * @throws Exception 
     * 
      ***************************************************** */
	public IMonitorDataGeneral getMonitorData() throws ZoneMinderInvalidData {
		    
		ZoneMinderContentResponse response = null;
		ZoneMinderMonitorData monitorData = null;
		     try {
		    	 
		     	//TODO HArdcoded values
 	            String path = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
                response = getConnection().getPageContent(buildUriApi(path), null);

                monitorData = IZoneMinderResponse.createFromJson(response.getContentAsJsonObject().getAsJsonObject("monitor").getAsJsonObject("Monitor"), 
                												response.getHttpStatus(), 
                												response.getHttpResponseMessage(), 
                												response.getHttpRequestURI(), 
                												ZoneMinderMonitorData.class);


		     } catch (IOException e) {
		    	 //TODO HAndle Error
		    	 //jsonObject = null;
		     
	  		 } catch (Exception e) {
		    	 //TODO HAndle Error
	  			 //jsonObject = null;
	  		 }
		    
		     return monitorData;
		    
	     }
	    
	     
	  public ZoneMinderMonitorStatusEnum getMonitorDetailedStatus() {
	    	 
             ZoneMinderContentResponse response = null;
             ZoneMinderMonitorStatus status = null;
             try {

 	        	//TODO HArdcoded values
 	            String path = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_MONITOR_ALARM_JSON, "MonitorId", getId());
                response = getConnection().getPageContent(buildUriApi(path), null);

                status = IZoneMinderResponse.createFromJson(response.getContentAsJsonObject(), 
	            		 								response.getHttpStatus(), 
	            		 								response.getHttpResponseMessage(), 
	            		 								response.getHttpRequestURI(), 
	            		 								ZoneMinderMonitorStatus.class);
	             if (status != null) {
	                 return status.getStatus();
	             }
	             
             } catch (IOException e) {
            	 //Intentionally left blank
             } catch (Exception e) {
            	//Intentionally left blank
             }
	         return ZoneMinderMonitorStatusEnum.UNKNOWN;
	     }
	     
	     

		@Override
		public ZoneMinderContentResponse SetEnabled(boolean enabled) throws MalformedURLException, ZoneMinderException {
			//TODO HArdcoded	
			String action = "";
			 if (enabled) {
				 action = "1";
			 }
			 else {
				 action = "0";
			 }

			JettyQueryParameter property = new JettyQueryParameter("Monitor[Enabled]", action);
			return setMonitorProperty(property );
		}


		
	/** *****************************************************
     * 
     * Monitor
	 * @throws MalformedURLException 
	 * @throws ZoneMinderException 
	 * @throws Exception 
     * 
      ***************************************************** */
	@Override
	public ZoneMinderContentResponse SetFunction(String function) throws MalformedURLException, ZoneMinderException {
			//TODO HArdcoded	
		JettyQueryParameter property = new JettyQueryParameter("Monitor[Function]", function);
		return setMonitorProperty(property );
	}

	public ZoneMinderContentResponse SetFunction(ZoneMinderMonitorFunctionEnum function) throws MalformedURLException, ZoneMinderException {
			//TODO HArdcoded	
		JettyQueryParameter property = new JettyQueryParameter("Monitor[Function]", function.toString());
		return setMonitorProperty(property );
	
	}

	private ZoneMinderContentResponse setMonitorProperty(JettyQueryParameter setting) throws MalformedURLException, ZoneMinderException {
		String methodPath = "";
		String action = "";
//		ArrayList<JettyQueryParameter> queryParam = new ArrayList<JettyQueryParameter>();
		//	TODO HArdcoded
		methodPath = replaceParameter(ZoneMinderServerConstants.SUBPATH_API_MONITOR_SPECIFIC_JSON, "MonitorId", getId());
		//queryParam.add(setting);
		//TODO Fix Fields
		Fields fields = new Fields();
		fields.add(setting.getName(), setting.getValue());
		return getConnection().sendPut(buildUriApi(methodPath), fields);	
	
	}




}
