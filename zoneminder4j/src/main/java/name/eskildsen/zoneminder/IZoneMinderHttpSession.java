package name.eskildsen.zoneminder;

import java.net.URI;

public interface IZoneMinderSession extends IZoneMinderCoreSession {

	boolean isAuthenticated();
	boolean isConnected();

		
}
