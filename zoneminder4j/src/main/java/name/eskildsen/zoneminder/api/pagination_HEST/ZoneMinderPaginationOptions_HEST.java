package name.eskildsen.zoneminder.api.pagination_HEST;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class ZoneMinderPaginationOptions_HEST {


	@SerializedName("order")
	@Expose
	public ZoneMinderPaginationOrder_HEST order;

}
