package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IZoneMinderHostLoad extends  IZoneMinderResponse {
	Float getCpuLoad();
}
