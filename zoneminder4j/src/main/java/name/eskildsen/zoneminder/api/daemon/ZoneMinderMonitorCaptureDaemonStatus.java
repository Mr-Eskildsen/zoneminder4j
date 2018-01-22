package name.eskildsen.zoneminder.api.daemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;

public class ZoneMinderMonitorCaptureDaemonStatus extends ZoneMinderDaemonStatus implements IZoneMinderDaemonStatus {
	
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
