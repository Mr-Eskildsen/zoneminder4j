package name.eskildsen.zoneminder.common;

public enum ZoneMinderMonitorStatusEnum {
	IDLE("Idle"),
	PRE_ALARM("Pre-Alarm"),
	ALARM("Alarm"),
	ALERT("Alert"),
    RECORDING("Recording"),
    UNKNOWN("Unknown");

	
	private String name;
	
	 /**
     * @param name
     */
	ZoneMinderMonitorStatusEnum(final String name) {
		this.name = name;
    }

    @Override
    public String toString() {
            return name;
    }

    public static ZoneMinderMonitorStatusEnum getEnumFromName(String value) {
        if (value.equalsIgnoreCase("Idle")) {
            return IDLE;
        } else if (value.equals("Pre-Alarm")) {
            return PRE_ALARM;
        } else if (value.equals("Alarm")) {
            return ALARM;
        } else if (value.equals("Alert")) {
            return ALERT;
        } else if (value.equals("Recording")) {
            return RECORDING;
        }
        return UNKNOWN;

    }

    public static ZoneMinderMonitorStatusEnum getEnumFromCode(String value) {

        if (value.equalsIgnoreCase("0")) {
            return IDLE;
        } else if (value.equals("1")) {
            return PRE_ALARM;
        } else if (value.equals("2")) {
            return ALARM;
        } else if (value.equals("3")) {
            return ALERT;
        } else if (value.equals("4")) {
            return RECORDING;
        }
        return UNKNOWN;

    }
}
