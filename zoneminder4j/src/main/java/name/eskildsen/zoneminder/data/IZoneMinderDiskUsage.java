package name.eskildsen.zoneminder.data;

import name.eskildsen.zoneminder.IZoneMinderResponse;

public interface IZoneMinderDiskUsage extends  IZoneMinderResponse {
    String getDiskUsage();

}
