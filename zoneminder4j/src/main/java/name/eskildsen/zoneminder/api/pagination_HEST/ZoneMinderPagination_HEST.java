package name.eskildsen.zoneminder.api.pagination_HEST;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class ZoneMinderPagination_HEST {

	@SerializedName("page")
	@Expose
	private Integer page;
	
	@SerializedName("current")
	@Expose
	private Integer current;
	
	@SerializedName("count")
	@Expose
	private Integer count;

	@SerializedName("prevPage")
	@Expose
	private Boolean prevPage;
	
	@SerializedName("nextPage")
	@Expose
	private Boolean nextPage;
	@SerializedName("pageCount")
	
	@Expose
	private Integer pageCount;
	
	@SerializedName("order")
	@Expose
	private ZoneMinderPaginationOrder_HEST order;
	
	@SerializedName("limit")
	@Expose
	private Integer limit;
	
	@SerializedName("options")
	@Expose
	private ZoneMinderPaginationOptions_HEST options;
	
	@SerializedName("paramType")
	@Expose
	private String paramType;
	
	
	
	public int getPageCount() {
		return pageCount;
	}

}
