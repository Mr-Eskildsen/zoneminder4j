package name.eskildsen.zoneminder;

import java.net.URI;

import name.eskildsen.zoneminder.internal.HttpRequest;

public interface IZoneMinderSession extends IZoneMinderCoreSession {

	boolean isAuthenticated();
	boolean isConnected();

}
