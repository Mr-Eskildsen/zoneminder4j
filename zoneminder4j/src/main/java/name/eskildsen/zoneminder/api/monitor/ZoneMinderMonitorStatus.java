package name.eskildsen.zoneminder.api.monitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;

public class ZoneMinderMonitorStatus {

	@SerializedName("status")
	@Expose
	private String status;
	
	public ZoneMinderMonitorStatusEnum getStatus() {
		return  ZoneMinderMonitorStatusEnum.getEnum(status);
	}
	
	
}
