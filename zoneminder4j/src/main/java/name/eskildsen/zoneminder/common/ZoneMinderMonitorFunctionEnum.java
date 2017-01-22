package name.eskildsen.zoneminder.common;

public enum ZoneMinderMonitorFunctionEnum {

    NONE, MONITOR, MODECT, RECORD, MOCORD, NODECT;

    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
	}
    
    public static ZoneMinderMonitorFunctionEnum getEnum(String value) {
        for(ZoneMinderMonitorFunctionEnum v : values())
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
