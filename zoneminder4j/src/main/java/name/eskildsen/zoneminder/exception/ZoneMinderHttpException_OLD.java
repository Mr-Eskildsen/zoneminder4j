package name.eskildsen.zoneminder.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ZoneMinderHttpException_OLD extends ZoneMinderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZoneMinderHttpException_OLD() {
		super("", null);
	}

	public ZoneMinderHttpException_OLD(String message) {
		super(message, null);
	}

	@SerializedName("class")
	@Expose
	private String _class;
	@SerializedName("code")
	@Expose
	private Integer code;
	@SerializedName("message")
	@Expose
	private String message;

	public String getClass_() {
	return _class;
	}

	public void setClass_(String _class) {
	this._class = _class;
	}

	public Integer getCode() {
	return code;
	}

	public void setCode(Integer code) {
	this.code = code;
	}

	public String getMessage() {
	return message;
	}

	public void setMessage(String message) {
	this.message = message;
	}

}
