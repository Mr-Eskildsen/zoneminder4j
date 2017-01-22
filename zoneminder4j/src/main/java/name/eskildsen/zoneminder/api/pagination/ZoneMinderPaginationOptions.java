package name.eskildsen.zoneminder.api.pagination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class ZoneMinderPaginationOptions {


	@SerializedName("order")
	@Expose
	public ZoneMinderPaginationOrder order;

}
