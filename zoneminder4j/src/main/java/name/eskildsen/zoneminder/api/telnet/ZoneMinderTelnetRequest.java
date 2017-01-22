package name.eskildsen.zoneminder.api.telnet;

import java.util.Arrays;
import java.util.List;

import name.eskildsen.zoneminder.internal.TelnetAction;


public class ZoneMinderTelnetRequest extends ZoneMinderTelnetBaseMessage {

    private static final Integer IDX_PRIORITY = 2;
    private static final Integer IDX_REASON = 3;
    private static final Integer IDX_NOTE = 4;
    private static final Integer IDX_SHOWTEXT = 5;

    private static final String COMMAND_PRIORITY = "priority";
    private static final String COMMAND_REASON = "reason";
    private static final String COMMAND_NOTE = "note";
    private static final String COMMAND_SHOWTEXT = "showText";

    private Integer priority;
    private String reason;
    private String note;
    private String showText;

    protected ZoneMinderTelnetRequest(String command) {
        super(command);
    }

    public ZoneMinderTelnetRequest(TelnetAction action, String monitorId, Integer priority, String reason,
            String note, String showText, Integer timeout) {
        super(action, monitorId, timeout);
        this.priority = priority;
        this.reason = reason;
        this.note = note ;
        this.showText = showText;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getReason() {
        return reason;
    }

    public String getNote() {
        return note;
    }

    public String getShowText() {
        return showText;
    }

    @Override
    protected void onParseCommand(String[] commandParts) {
        priority = null;
        reason = null;
        note = null;
        showText = null;

        priority = Integer.parseInt(commandParts[IDX_PRIORITY]);
        if (commandParts.length > IDX_REASON) {
            reason = fixSpaces(commandParts[IDX_REASON]);
            if (commandParts.length > IDX_NOTE) {
                note = fixSpaces(commandParts[IDX_NOTE]);
                if (commandParts.length > IDX_SHOWTEXT) {
                    showText = fixSpaces(commandParts[IDX_SHOWTEXT]);
                }
            }
        }
    }

    @Override
    protected String onBuildSubCommandString(String command) {
        switch (command) {
            case COMMAND_PRIORITY:
                return convertToString(priority);
            case COMMAND_REASON:
                return reason;
            case COMMAND_NOTE:
                return note;
            case COMMAND_SHOWTEXT:
                return showText;
        }
        return null;
    }

    @Override
    protected List<String> getCommandArray() {
        return Arrays.asList(COMMAND_MONITORID, COMMAND_ACTION, COMMAND_PRIORITY, COMMAND_REASON, COMMAND_NOTE,
                COMMAND_SHOWTEXT);
    }

}
