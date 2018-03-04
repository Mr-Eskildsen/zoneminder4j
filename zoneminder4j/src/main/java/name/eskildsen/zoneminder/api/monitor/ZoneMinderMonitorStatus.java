package name.eskildsen.zoneminder.api.monitor;

import name.eskildsen.zoneminder.IZoneMinderResponse;
import name.eskildsen.zoneminder.common.ZoneMinderMonitorStatusEnum;

public interface ZoneMinderMonitorStatus extends  IZoneMinderResponse {

	ZoneMinderMonitorStatusEnum getStatus();
}
