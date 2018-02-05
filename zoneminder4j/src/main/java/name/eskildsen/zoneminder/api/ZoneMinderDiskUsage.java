package name.eskildsen.zoneminder.api;

import java.net.URI;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import name.eskildsen.zoneminder.IZoneMinderDiskUsage;
import name.eskildsen.zoneminder.annotation.ZoneMinderJson;
/*
 * 
{"usage":{"Baghave":{"space":"1.8429298400879","color":"red"},"Indkoersel":{"space":"13.142463684082","color":"red"},"Terrace":{"space":"0.41262435913086","color":"red"},"Total":{"space":"23.315731048584","color":"#F7464A"}}}
 */

public class ZoneMinderDiskUsage extends ZoneMinderCoreData implements IZoneMinderDiskUsage {

	
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
