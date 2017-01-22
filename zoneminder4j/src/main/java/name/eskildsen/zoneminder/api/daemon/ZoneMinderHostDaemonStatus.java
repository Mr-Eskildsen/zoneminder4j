package name.eskildsen.zoneminder.api.daemon;

import name.eskildsen.zoneminder.IZoneMinderDaemonStatus;
import name.eskildsen.zoneminder.api.ZoneMinderData;

public class ZoneMinderHostDaemonStatus extends ZoneMinderDaemonStatus implements IZoneMinderDaemonStatus {
	private Boolean _daemonState = false;
	
	protected ZoneMinderHostDaemonStatus(Integer daemonState) {
		_daemonState = ((daemonState==1) ? true : false);
	}
	
	@Override
	public boolean getStatus() {
		return _daemonState;
	}
}
