package name.eskildsen.zoneminder.api.daemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;

public class ZoneMinderMonitorAnalysisDaemonStatus extends ZoneMinderDaemonStatus implements IZoneMinderDaemonStatus {
/*
	protected ZoneMinderMonitorAnalysisDaemonStatus(Integer state, String statusText) {
	
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
