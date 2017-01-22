package name.eskildsen.zoneminder.api.event;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderEventData;
import name.eskildsen.zoneminder.api.ZoneMinderData;

public class ZoneMinderEvent extends ZoneMinderData implements IZoneMinderEventData {

	@SerializedName("Id")
	@Expose
	private String id;
	
	@SerializedName("MonitorId")
	@Expose
	private String monitorId;
	
	@SerializedName("Name")
	@Expose
	private String name;
	
	@SerializedName("Cause")
	@Expose
	private String cause;
	
	@SerializedName("StartTime")
	@Expose
	private String startTime;
	
	@SerializedName("EndTime")
	@Expose
	private String endTime;
	
	@SerializedName("Width")
	@Expose
	private String width;
	
	@SerializedName("Height")
	@Expose
	private String height;
	
	@SerializedName("Length")
	@Expose
	private String length;
	
	@SerializedName("Frames")
	@Expose
	private String frames;
	@SerializedName("AlarmFrames")
	@Expose
	private String alarmFrames;
	@SerializedName("TotScore")
	@Expose
	private String totScore;
	@SerializedName("AvgScore")
	@Expose
	private String avgScore;
	@SerializedName("MaxScore")
	@Expose
	private String maxScore;
	@SerializedName("Archived")
	@Expose
	private String archived;
	@SerializedName("Videoed")
	@Expose
	private String videoed;
	@SerializedName("Uploaded")
	@Expose
	private String uploaded;
	@SerializedName("Emailed")
	@Expose
	private String emailed;
	@SerializedName("Messaged")
	@Expose
	private String messaged;
	@SerializedName("Executed")
	@Expose
	private String executed;
	@SerializedName("Notes")
	@Expose
	private String notes;


	public String getId()
	{
		return id;
	}

	public String getCause()
	{
		return cause;
	}


	public String getStartTime() 
	{
		return startTime;
	}
	

	public String getEndTime() 
	{
		return endTime;
	}
	
}

