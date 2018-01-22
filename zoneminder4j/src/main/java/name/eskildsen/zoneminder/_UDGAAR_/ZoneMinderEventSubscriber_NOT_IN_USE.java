package name.eskildsen.zoneminder.trigger;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderEventSubscriber;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;
import name.eskildsen.zoneminder.internal.ZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.internal.ZoneMinderSession;

public class ZoneMinderEventSubscriber_NOT_IN_USE implements IZoneMinderEventSubscriber {
	private ZoneMinderSession sessionTelnet = null; 
	private ZoneMinderConnectionInfo connectionTelnet = null;
	
	public ZoneMinderEventSubscriber_NOT_IN_USE(IZoneMinderConnectionInfo connection)
	{
		connectionTelnet = (ZoneMinderConnectionInfo)connection;
		
	}
	
	@Override
	public void onTrippedForceAlarm(ZoneMinderTriggerEvent event) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void SubscribeMonitorEvents(String monitorId) {
		// TODO Auto-generated method stub
		
	}

	//	@Override
	public void UnsubscribeMonitorEvents(String monitorId) {
		// TODO Auto-generated method stub
		
	}

}
