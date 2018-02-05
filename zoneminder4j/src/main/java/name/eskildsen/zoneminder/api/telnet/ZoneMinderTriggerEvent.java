/**
 * Copyright (c) 2014-2016 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package name.eskildsen.zoneminder.api.telnet;

import java.util.Arrays;
import java.util.List;

import name.eskildsen.zoneminder.event.ZoneMinderEventAction;


public class ZoneMinderTriggerEvent extends ZoneMinderTelnetBaseMessage {
    private static final Integer IDX_UNKNOWN1 = 2;
    private static final Integer IDX_EVENTID = 3;

    private static final String COMMAND_UNKNOWN1 = "unknown1";
    private static final String COMMAND_EVENTID = "eventId";

    private String unknown1;
    private String eventId;

    public ZoneMinderTriggerEvent(String command) {
        super(command);
    }

 
    
    @Override
    protected void onParseCommand(String[] commandParts) {
        unknown1 = commandParts[IDX_UNKNOWN1];
        eventId = commandParts[IDX_EVENTID];

    }

    @Override
    protected String onBuildSubCommandString(String command) {
        switch (command) {
            case COMMAND_UNKNOWN1:
                return unknown1;
            case COMMAND_EVENTID:
                return eventId;
        }
        return null;
    }

    @Override
    protected List<String> getCommandArray() {
        return Arrays.asList(COMMAND_MONITORID, COMMAND_ACTION, COMMAND_UNKNOWN1, COMMAND_EVENTID);
    }

    public String getEventId() {
        return eventId;
    }


}
