package name.eskildsen.zoneminder.trigger;

import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;

interface TriggerSubscriberInterface
{
	
	public abstract void onTripped(ZoneMinderTriggerEvent event);
    
 
}