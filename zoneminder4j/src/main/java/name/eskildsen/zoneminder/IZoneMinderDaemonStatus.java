package name.eskildsen.zoneminder;

public interface IZoneMinderDaemonStatus extends  IZoneMinderResponse {
	boolean getStatus();
	String getStatusText();
}
