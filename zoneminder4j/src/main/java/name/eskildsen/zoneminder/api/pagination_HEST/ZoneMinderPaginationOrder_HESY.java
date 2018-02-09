package name.eskildsen.zoneminder.api.pagination_HEST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ZoneMinderPaginationOrder_HEST {

	@SerializedName("Event.StartTime")
	@Expose
	public String eventStartTime;
	@SerializedName("Event.MaxScore")
	@Expose
	public String eventMaxScore;

}
