package name.eskildsen.zoneminder.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public class ZoneMinderConnectionPool extends ObjectPool<ZoneMinderSession> {
	private ZoneMinderConnectionInfo connection = null;
				
	public ZoneMinderConnectionPool(ZoneMinderConnectionInfo conn, Integer initialSize, Integer maxSize){
		super( initialSize, maxSize, 300);

		connection = conn;
		
		try {
			initialize(1);
		} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException | ZoneMinderApiNotEnabledException e) {
					//Just ignore it here
		}
	}
			
	
	@Override
	protected ZoneMinderSession onCreate() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException {
		ZoneMinderSession session = null;
		try {
			session = new ZoneMinderSession(connection, true, false);
		} catch (IllegalArgumentException  eW) {
			
		}
		return session;
	}

	@Override
	public boolean validate(ZoneMinderSession o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void expire(ZoneMinderSession o) {
		// TODO Auto-generated method stub
		
	}

}
