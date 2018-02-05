package name.eskildsen.zoneminder.exception.http;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestExceptionEx {

@SerializedName("class")
@Expose
private String _class;

@SerializedName("code")
@Expose
private Integer code;

@SerializedName("message")
@Expose
private String message;

@SerializedName("trace")
@Expose
private List<String> trace = null;

public String getClass_() {
return _class;
}

public void setClass_(String _class) {
this._class = _class;
}

public Integer getHttpStatus() {
return code;
}

public String getMessage() {
return message;
}
/*
public void setMessage(String message) {
this.message = message;
}
*/
public List<String> getTrace() {
return trace;
}

public void setTrace(List<String> trace) {
this.trace = trace;
}

}