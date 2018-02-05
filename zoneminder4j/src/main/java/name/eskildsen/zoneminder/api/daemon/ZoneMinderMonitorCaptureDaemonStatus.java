package name.eskildsen.zoneminder.api.daemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.ZoneMinderCoreData;

public class ZoneMinderMonitorCaptureDaemonStatus extends ZoneMinderCoreData implements IZoneMinderDaemonStatus {
	
	@SerializedName("status")
	@Expose
	private Boolean status;
	
	
	@SerializedName("statustext")
	@Expose
	private String statusText;

	public boolean getStatus() {
		return status;
	}
	
	
	public String getStatusText() {
		return statusText;
	}

/*	protected ZoneMinderMonitorCaptureDaemonStatus(Integer state, String statusText) {
		
	}
	*/
}
