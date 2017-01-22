package name.eskildsen.zoneminder.internal;

import java.io.IOException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.trigger.ZoneMinderEventNotifier;

public abstract class ZoneMinderGenericSession extends ZoneMinderEventNotifier {
	private ZoneMinderConnectionInfo _connection = null;
    private Boolean connected = false;

    protected ZoneMinderGenericSession(ZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException
    {
    	_connection = connection;  
    	connect();
    }

    public ZoneMinderConnectionInfo getConnectionInfo() {return _connection;}
    
    public synchronized Boolean isConnected() {
        return this.connected;
    }

    protected boolean getConnected()
    {
    	return connected;
    }
    
    protected void setConnected(Boolean connected) {
    	Boolean stateChanged = false;
    	synchronized (connected) {
    		if (this.connected != connected)
    			stateChanged = true;
    		this.connected = connected;	
		} 
    	
    	if (stateChanged) {
    		if (!connected) {
    			closeConnection();
    		}
    	}
        
    }

    
	protected abstract Boolean connect() throws IOException, IllegalArgumentException, ZoneMinderUrlNotFoundException, FailedLoginException ;
	protected abstract void closeConnection();

}

