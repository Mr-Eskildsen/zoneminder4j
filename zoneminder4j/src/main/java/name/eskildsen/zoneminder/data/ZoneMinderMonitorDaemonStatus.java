package name.eskildsen.zoneminder.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneMinderMonitorDaemonStatus extends ZoneMinderCoreData implements IZoneMinderDaemonStatus {
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
