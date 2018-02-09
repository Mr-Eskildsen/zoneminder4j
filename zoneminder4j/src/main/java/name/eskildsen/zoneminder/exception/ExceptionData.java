package name.eskildsen.zoneminder.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExceptionData {

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("message")
	@Expose
	private String message;

	@SerializedName("url")
	@Expose
	private String url;

	@SerializedName("exception")
	@Expose
	private RequestExceptionEx exception;

	// @SerializedName("queryLog")
	// @Expose
	// private QueryLog queryLog;

	protected RequestExceptionEx getExtendedInfo() {
		return exception;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	/*
	 * public Exception getException() { return exception; }
	 * 
	 * public void setException(Exception exception) { this.exception = exception; }
	 * 
	 * public QueryLog getQueryLog() { return queryLog; }
	 * 
	 * public void setQueryLog(QueryLog queryLog) { this.queryLog = queryLog; }
	 */
}