package name.eskildsen.zoneminder.api.pagination;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ZoneMinderPaginationOrder {

	@SerializedName("Event.StartTime")
	@Expose
	public String eventStartTime;
	@SerializedName("Event.MaxScore")
	@Expose
	public String eventMaxScore;

}
