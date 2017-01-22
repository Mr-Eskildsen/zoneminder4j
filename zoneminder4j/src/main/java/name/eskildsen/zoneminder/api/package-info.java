/**
 *	Host API 
 * ==================
 * 		/host/getVersion.json
   		/host/getLoad.json
   		/host/getDiskPercent.json
   		/host/daemonCheck.json

 *	Monitor API
 *  ==================
 *    	/monitors.json";
    	/monitors/{MonitorId}.json";
    	/monitors/daemonStatus/id:{MonitorId}/daemon:{DaemonName}.json";

 *
 *  Event API
 *  ==================
 *		/events/index/MonitorId:{MonitorId}.json  
 *  
 *  http://server/zm/api/events/events/index/MonitorId:5/StartTime >=:2015-05-15 18:43:56/EndTime <=:2015-05-16 18:43:56.json
	To try this in CuRL, you need to URL escape the spaces like so:
	curl -XGET  "http://server/zm/api/events/index/MonitorId:5/StartTime%20>=:2015-05-15%2018:43:56/EndTime%20<=:2015-05-16%2018:43:56.json"

	Return a list of events for all monitors within a specified date/time range
	curl -XGET "http://server/zm/api/events/index/StartTime%20>=:2015-05-15%2018:43:56/EndTime%20<=:208:43:56.json"

	Return event count based on times and conditions
	The API also supports a handy mechanism to return a count of events for a period of time.

	This returns number of events per monitor that were recorded in the last one hour
	curl "http://server/zm/api/events/consoleEvents/1%20hour.json"

	This returns number of events per monitor that were recorded in the last day where there were atleast 10 frames that were alarms”
	curl "http://server/zm/api/events/consoleEvents/1%20day.json/AlarmFrames >=: 10.json"
 *  
 * 
 * 
 * @author martin
 *
 */
package name.eskildsen.zoneminder.api;