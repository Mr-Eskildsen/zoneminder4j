package name.eskildsen.zoneminder;


import java.net.MalformedURLException;

import name.eskildsen.zoneminder.common.ZoneMinderServerConstants;
import name.eskildsen.zoneminder.event.ZoneMinderEventManager;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderGeneralException;
import name.eskildsen.zoneminder.exception.ZoneMinderInvalidData;
import name.eskildsen.zoneminder.exception.ZoneMinderResponseException;
import name.eskildsen.zoneminder.internal.ZoneMinderMonitorProxyImpl;
import name.eskildsen.zoneminder.internal.ZoneMinderServerProxy;
import name.eskildsen.zoneminder.jetty.JettyConnectionInfo;

public interface ZoneMinderFactory {

	public static String getDefaultPortalSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_PORTAL_SUBPATH;
	}
	
	public static String getDefaultApiSubpath()
	{
		return ZoneMinderServerConstants.DEFAULT_API_SUBPATH;
	}
	
	
	public static IZoneMinderConnectionHandler CreateConnection(String protocol, String hostName, Integer portHttp, Integer portTelnet, String userName,
            String password, String streamingUserName, String streamingPassword,  String zmPortalSubPath, String zmApiSubPath, Integer timeout) throws ZoneMinderApiNotEnabledException, ZoneMinderAuthenticationException, ZoneMinderGeneralException, MalformedURLException, ZoneMinderInvalidData, ZoneMinderResponseException {
		
		JettyConnectionInfo jci =new JettyConnectionInfo(protocol, hostName, portHttp, portTelnet, userName, password, streamingUserName, streamingPassword,  zmPortalSubPath, zmApiSubPath, timeout);
		if (jci.connect()) {
			return jci;	
		}
		return null;
		 
	}


	public static IZoneMinderEventSession CreateEventSession(IZoneMinderConnectionHandler connection)
	{

		return new ZoneMinderEventManager(connection); //ZoneMinderMonitorProxy(session, monitorId);
	}

	
	public static IZoneMinderServer getServerProxy(IZoneMinderConnectionHandler connection)
	{
		return new ZoneMinderServerProxy(connection);
	}
	

	public static IZoneMinderMonitor getMonitorProxy(IZoneMinderConnectionHandler connection, String monitorId)
	{
		return new ZoneMinderMonitorProxyImpl(connection, monitorId);
	}
	
}
