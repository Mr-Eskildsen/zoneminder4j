package name.eskildsen.zoneminder.exception.http;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.exception.ZoneMinderException;

public class ZoneMinderResponseException extends ZoneMinderException {

	public ZoneMinderResponseException() {
		super("", null);
	}

	@SerializedName("success")
	@Expose
	private Boolean success;

	@SerializedName("data")
	@Expose
	private ExceptionData data;
	
	
	
	public Integer getHttpStatus() {
		return data.getExtendedInfo().getHttpStatus();
	}

	public String getHttpMessage() {
		return data.getExtendedInfo().getMessage();
	}
/*
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public ExceptionData getData() {
		return data;
	}

	public void setData(ExceptionData data) {
		this.data = data;
	}
*/
}