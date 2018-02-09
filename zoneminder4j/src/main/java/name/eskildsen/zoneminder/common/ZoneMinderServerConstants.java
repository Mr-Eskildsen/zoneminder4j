package name.eskildsen.zoneminder.common;

public class ZoneMinderServerConstants {
	public static final String DEFAULT_PORTAL_SUBPATH = "/zm";
	public static final String DEFAULT_API_SUBPATH = "/zm/api";
	
    public static final String SUBPATH_SERVERLOGIN = "/index.php";
    
    
    public static final String SUBPATH_API_HOST_VERSION_JSON = "/host/getVersion.json";
    public static final String SUBPATH_API_HOST_CPULOAD_JSON = "/host/getLoad.json";
    public static final String SUBPATH_API_HOST_DISKPERCENT_JSON = "/host/getDiskPercent.json";
    public static final String SUBPATH_API_HOST_DAEMON_CHECKSTATE = "/host/daemonCheck.json";

    public static final String SUBPATH_API_MONITORS_JSON = "/monitors.json";
    public static final String SUBPATH_API_MONITOR_SPECIFIC_JSON = "/monitors/{MonitorId}.json";
    public static final String SUBPATH_API_MONITOR_DAEMONSTATUS_JSON = "/monitors/daemonStatus/id:{MonitorId}/daemon:{DaemonName}.json";
    
    public static final String SUBPATH_API_MONITOR_ALARM_JSON = "/monitors/alarm/id:{MonitorId}/command:status.json"; 
    
    public static final String SUBPATH_API_EVENTS_SPECIFIC_MONITOR_JSON = "/events/index/MonitorId:{MonitorId}.json";
    public static final String SUBPATH_API_EVENTS_SPECIFIC_EVENT_JSON = "/events/index/Id:{EventId}.json";
    
    public static final String SUBPATH_API_SERVER_GET_CONFIG_JSON = "/configs/view/{ConfigId}.json";
    public static final String SUBPATH_API_SERVER_SET_CONFIG_JSON = "/configs/edit/{ConfigId}.json";

    
}
