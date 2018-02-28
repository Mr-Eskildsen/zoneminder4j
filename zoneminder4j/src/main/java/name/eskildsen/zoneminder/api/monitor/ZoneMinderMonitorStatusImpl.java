package name.eskildsen.zoneminder.api.monitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;
import name.eskildsen.zoneminder.data.ZoneMinderCoreData;
import name.eskildsen.zoneminder.internal.ZoneMinderContentResponse;

//TODO Add Interface
public class ZoneMinderMonitorStatusImpl extends ZoneMinderCoreData implements ZoneMinderMonitorStatus {

	@SerializedName("status")
	@Expose
	private String status;
	
	public ZoneMinderMonitorStatusImpl (){
	}

	
	public ZoneMinderMonitorStatusImpl (ZoneMinderMonitorStatusEnum status, ZoneMinderContentResponse zmcr){
		super(zmcr.getHttpStatus(), zmcr.getHttpResponseMessage(), zmcr.getHttpRequestURI());
		this.status = status.name();
	}
	public ZoneMinderMonitorStatusEnum getStatus() {
		return  ZoneMinderMonitorStatusEnum.getEnumFromCode(status);
	}


}
