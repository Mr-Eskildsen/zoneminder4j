package name.eskildsen.zoneminder.event;

import name.eskildsen.zoneminder.api.event.ZoneMinderEvent;

public interface ZoneMinderMonitorEventListener {

    //void notifyZoneMinderApiDataUpdated(ThingTypeUID thingTypeUID, String ZoneMinderId, ZoneMinderData data);

    void notifyZoneMinderEvent(ZoneMinderEvent event);
}