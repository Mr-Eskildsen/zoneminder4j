package name.eskildsen.zoneminder.common;

public enum ZoneMinderMonitorEventEnum {

	NONE("None"),
	MOTION("Motion"),
	SIGNAL("Signal"),
	FORCED_WEB("Forced Web"),
    OPENHAB("openHAB"),
	OTHER("Other");

	private String name;
	 /**
     * @param name
     */
	ZoneMinderMonitorEventEnum(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
            return name;
    }

    public static ZoneMinderMonitorEventEnum getEnum(String value) {

        if (value.equalsIgnoreCase(NONE.toString())) {
            return NONE;
        } else if (value.equalsIgnoreCase(FORCED_WEB.toString())) {
            return FORCED_WEB;
        } else if (value.equalsIgnoreCase(MOTION.toString())) {
            return MOTION;
        } else if (value.equalsIgnoreCase(SIGNAL.toString())) {
            return SIGNAL;
        }
        return ZoneMinderMonitorEventEnum.OTHER;

    }

}
