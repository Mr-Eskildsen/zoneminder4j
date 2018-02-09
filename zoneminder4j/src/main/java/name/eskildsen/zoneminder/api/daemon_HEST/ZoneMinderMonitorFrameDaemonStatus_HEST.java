package name.eskildsen.zoneminder.api.daemon_HEST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.data.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;

abstract class ZoneMinderMonitorFrameDaemonStatus_HEST extends ZoneMinderCoreData implements IZoneMinderDaemonStatus {
/*
	protected ZoneMinderMonitorFrameDaemonStatus(Integer state, String statusText) {
	
		
	}
	*/
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

}
