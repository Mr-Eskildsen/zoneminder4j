package name.eskildsen.zoneminder.trigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderEventSubscriber;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public abstract class ZoneMinderEventNotifier  {
	    private Map<String, List<IZoneMinderEventSubscriber>> subscriberMap = new HashMap<String, List<IZoneMinderEventSubscriber>>();

	    
	    protected abstract boolean startTelnetListener(IZoneMinderConnectionInfo conenction) throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException;
	    protected abstract boolean stopTelnetListener();

	    public synchronized void SubscribeMonitorEvents(IZoneMinderConnectionInfo conenction, String monitorId, IZoneMinderEventSubscriber subscriber) throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException { 

	    	List<IZoneMinderEventSubscriber> monitorSubscriber = null;
	    	
	    	if (subscriberMap.size()==0) {
	    		startTelnetListener(conenction);
	    	}
	    	
	    	if (subscriberMap.containsKey(monitorId)) {
	    		monitorSubscriber = subscriberMap.get(monitorId);
	    		subscriberMap.remove(monitorId);
	    		
	    	}
	    	else {
	    		monitorSubscriber = new ArrayList<IZoneMinderEventSubscriber>();
	    		
	    	}
	    	monitorSubscriber.add(subscriber);
	    	subscriberMap.put(monitorId, monitorSubscriber);
	    } 	
	    
	    
	    public synchronized void UnsubscribeMonitorEvents(String monitorId, IZoneMinderEventSubscriber subscriber) {
	    	
	    	List<IZoneMinderEventSubscriber> monitorSubscriber = null;
	    	
	    	if (subscriberMap.containsKey(monitorId)) {
	    		monitorSubscriber = subscriberMap.get(monitorId);
	    		subscriberMap.remove(monitorId);
	    		
	    		monitorSubscriber.remove(subscriber);
	    	
		    	//If others is subscribing for this monitor
		    	if (monitorSubscriber.size()>0) {
		    		subscriberMap.put(monitorId, monitorSubscriber);
		    	}
		    	
		    	if (subscriberMap.size()==0) {
		    		stopTelnetListener();
		    	}
	    	}

	    	
	    	
	    }
	    

	    protected synchronized void tripMonitor(ZoneMinderTriggerEvent event)
	    {
	    	if (subscriberMap.containsKey(event.getMonitorId())) {
	    		List<IZoneMinderEventSubscriber> subscribers = subscriberMap.get(event.getMonitorId());
	    		
	    		for (IZoneMinderEventSubscriber subscriber : subscribers) {
	    			subscriber.onTrippedForceAlarm(event);	
	    		}
	    		
	    	}
    
	    } 

}
