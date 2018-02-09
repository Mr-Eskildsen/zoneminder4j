package name.eskildsen.zoneminder.data;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneMinderHostLoad extends ZoneMinderCoreData implements IZoneMinderHostLoad {

	@SerializedName("load")
	@Expose
	private List<Float> load = new ArrayList<Float>();


	public Float getCpuLoad() {
		if (load.size()>0) {
			return load.get(2);
		}
		return null;
	}
}
