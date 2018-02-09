package name.eskildsen.zoneminder.api.monitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;

//TODO Add Interface
public class ZoneMinderMonitorStatus extends ZoneMinderCoreData {

	@SerializedName("status")
	@Expose
	private String status;

	public ZoneMinderMonitorStatusEnum getStatus() {
		return  ZoneMinderMonitorStatusEnum.getEnum(status);
	}


}
