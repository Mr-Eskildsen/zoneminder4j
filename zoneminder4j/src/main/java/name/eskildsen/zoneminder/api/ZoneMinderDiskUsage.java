package name.eskildsen.zoneminder.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderDiskUsage;


public class ZoneMinderDiskUsage extends ZoneMinderResponseData implements IZoneMinderDiskUsage {

	
	@SerializedName("space")
	@Expose
	private String space;
	
	@SerializedName("color")
	@Expose
	private String color;

	
    public String getDiskUsage() {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
        df.setDecimalFormatSymbols(dfs);
        return df.format(Double.parseDouble(space));

    }
}
