package name.eskildsen.zoneminder;

import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;

public interface IZoneMinderEventSubscriber { 

	
    void onTrippedForceAlarm(ZoneMinderTriggerEvent event);
}
