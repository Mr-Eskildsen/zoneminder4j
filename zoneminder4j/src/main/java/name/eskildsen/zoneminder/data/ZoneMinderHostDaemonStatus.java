package name.eskildsen.zoneminder.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneMinderHostDaemonStatus extends ZoneMinderCoreData implements IZoneMinderDaemonStatus {
	
	@SerializedName("result")
	@Expose
	private Integer _result; // = false;

	
	/* TODO:: GSON CRAP
	protected ZoneMinderHostDaemonStatus(Integer daemonState) {
		_daemonState = ((daemonState==1) ? true : false);
	}
	*/
	
	@Override
	public boolean getStatus() {
		return (_result == 1) ? true : false;
	}


	@Override
	public String getStatusText() {
		// TODO Auto-generated method stub
		return null;
	}
}
