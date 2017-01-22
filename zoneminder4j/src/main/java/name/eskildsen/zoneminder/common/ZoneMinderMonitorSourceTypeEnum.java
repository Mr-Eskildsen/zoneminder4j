package name.eskildsen.zoneminder.common;

public enum ZoneMinderMonitorSourceTypeEnum {
	LOCAL("Local"), REMOTE("Remote"), FFMPEG("Ffmpeg"), LIBVLC("Libvlc"), CURL("cURL (HTTP(S) only)");

	private String name = "";
	
	ZoneMinderMonitorSourceTypeEnum(String name) {
		this.name = name;
	}
    public String toString() {
        return name; // name().charAt(0) + name().substring(1).toLowerCase();
	}
    
    public static ZoneMinderMonitorSourceTypeEnum getEnum(String value) {
        for(ZoneMinderMonitorSourceTypeEnum v : values())
            if(v.toString().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }   
    
    public static Boolean isValid(String value) {
    	Boolean valid = false;
    	try {
    		getEnum(value);
    		valid = true;
    	}
    	catch(IllegalArgumentException ex) {
    		valid = false;
    	}
    	return valid;
    }

}
