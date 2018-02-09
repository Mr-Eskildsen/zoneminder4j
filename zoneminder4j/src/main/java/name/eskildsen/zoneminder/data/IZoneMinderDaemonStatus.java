package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IZoneMinderDaemonStatus extends  IZoneMinderResponse {
	boolean getStatus();
	String getStatusText();
}
