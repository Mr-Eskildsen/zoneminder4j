package name.eskildsen.zoneminder.data;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ZoneMinderHostVersion extends ZoneMinderCoreData implements IZoneMinderHostVersion {

	@SerializedName("version")
	@Expose
    protected String Version;

	@SerializedName("apiversion")
	@Expose
	protected String ApiVersion;

    public String getVersion() {
    	return Version;
    }
    
    
    public String getApiVersion() {
    	return ApiVersion;
    }
}
