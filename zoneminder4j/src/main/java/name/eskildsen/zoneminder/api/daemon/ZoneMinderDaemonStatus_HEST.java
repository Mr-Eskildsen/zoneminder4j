package name.eskildsen.zoneminder.api.daemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.api.ZoneMinderCoreData;

public abstract class ZoneMinderDaemonStatus_HEST extends ZoneMinderCoreData {

	
	
	public abstract boolean getStatus();
	

	/*
	public static ZoneMinderDaemonStatus Create(ZoneMinderDaemonType daemonType, Integer daemonState, String daemonStatusText){
		
		ZoneMinderDaemonStatus newDaemon = null;
		
		switch(daemonType) {
			case DAEMON_HOST:
				newDaemon = new ZoneMinderHostDaemonStatus(daemonState); 
				break;

			case DAEMON_MONITOR_CAPTURE:
				newDaemon = new ZoneMinderMonitorCaptureDaemonStatus(daemonState, daemonStatusText); 
				break;
	
			case DAEMON_MONITOR_ANALYSIS:
				newDaemon = new ZoneMinderMonitorAnalysisDaemonStatus(daemonState, daemonStatusText); 
				break;

			case DAEMON_MONITOR_FRAME:
				newDaemon = new ZoneMinderMonitorFrameDaemonStatus(daemonState, daemonStatusText); 
				break;
}

		return newDaemon;
	}
	*/
}
