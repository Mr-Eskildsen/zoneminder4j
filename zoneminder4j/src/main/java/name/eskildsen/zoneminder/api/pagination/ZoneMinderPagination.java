package name.eskildsen.zoneminder.api.pagination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class ZoneMinderPagination {

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
	private ZoneMinderPaginationOrder order;
	
	@SerializedName("limit")
	@Expose
	private Integer limit;
	
	@SerializedName("options")
	@Expose
	private ZoneMinderPaginationOptions options;
	
	@SerializedName("paramType")
	@Expose
	private String paramType;
	
	
	
	public int getPageCount() {
		return pageCount;
	}

}
