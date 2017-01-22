package name.eskildsen.zoneminder.api.host;

import name.eskildsen.zoneminder.IZoneMinderHostVersion;
import name.eskildsen.zoneminder.api.ZoneMinderData;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ZoneMinderHostVersion extends ZoneMinderData implements IZoneMinderHostVersion {

	@SerializedName("version")
	@Expose
    private String Version;

	@SerializedName("apiversion")
	@Expose
    private String ApiVersion;

    public String getVersion() {
    	return Version;
    }
    
    
    public String getApiVersion() {
    	return ApiVersion;
    }
}
