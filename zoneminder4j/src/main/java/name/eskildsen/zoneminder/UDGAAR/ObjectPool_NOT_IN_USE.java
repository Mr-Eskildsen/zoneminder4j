package name.eskildsen.zoneminder.internal;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.api.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.api.exception.ZoneMinderCredentialsMissingException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;

public abstract class ObjectPool<T> {
	Lock lock = new ReentrantLock();

	private long expirationTime;
	private Hashtable<T, Long> locked = null;
	private LinkedBlockingQueue<T> unlocked;
	private boolean connected = false;



	private ScheduledExecutorService executorService;
	private int iPoolSize = 0;
	private int iMinIdle = 1;
	private int iMaxIdle = 1;
	/**
	 * Creates the pool.
	 *
	 * @param minIdle minimum number of objects residing in the pool
	 */
	public ObjectPool(final int minIdle) {
		expirationTime = 30000; // 30 seconds

		iMinIdle = minIdle;
		if (minIdle>1)
			iMaxIdle = minIdle;

		// initialize pool
		try {
			initialize(0);
		} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException | ZoneMinderApiNotEnabledException|ZoneMinderCredentialsMissingException e) {
			//Just Ignore it here
		}
	}


	public void maintainPool() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException {
		int sizeToBeAdded = 0;
		int sizeToBeRemoved = 0;


		try {
			if (lock.tryLock(5L, TimeUnit.SECONDS)) {

				if (iPoolSize < iMinIdle) {
					sizeToBeAdded = iMinIdle - iPoolSize;
				} else if (iPoolSize > iMaxIdle) {
					sizeToBeRemoved = iPoolSize - iMaxIdle;
				}		    	
			} 

		} catch (InterruptedException e) {
			//We should never end here, unless somebody is trying to shut down
			return;
		} finally {
			lock.unlock();
		}







		for (int i = 0; i < sizeToBeAdded; i++) {
			unlocked.add(create());
		}

		for (int i = 0; i < sizeToBeRemoved; i++) {
			unlocked.poll();
			iPoolSize--;
		}
	}

	/**
	 * Creates the pool.
	 *
	 * @param minIdle            minimum number of objects residing in the pool
	 * @param maxIdle            maximum number of objects residing in the pool
	 * @param validationInterval time in seconds for periodical checking of minIdle / maxIdle conditions in a separate thread.
	 *                           When the number of objects is less than minIdle, missing instances will be created.
	 *                           When the number of objects is greater than maxIdle, too many instances will be removed.
	 */
	public ObjectPool(final int minIdle, final int maxIdle, final long validationInterval) {
		expirationTime = 30000; // 30 seconds
		iMinIdle = minIdle;
		iMaxIdle = maxIdle;
		// initialize pool
		try {
			initialize(0);
		} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException | ZoneMinderApiNotEnabledException |ZoneMinderCredentialsMissingException e1) {
			//Just ignore it here
		}


		// check pool conditions in a separate thread
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleWithFixedDelay(new Runnable()
		{
			@Override
			public void run() {
				try {
					maintainPool();
				} catch (FailedLoginException | IOException | ZoneMinderUrlNotFoundException | ZoneMinderApiNotEnabledException| ZoneMinderCredentialsMissingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, validationInterval, validationInterval, TimeUnit.SECONDS);


	}


	public abstract boolean validate(T o);

	public abstract void expire(T o);


	/**
	 * Gets the next free object from the pool. If the pool doesn't contain any objects,
	 * a new object will be created and given to the caller of this method back.
	 *
	 * @return T borrowed object
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws IOException 
	 * @throws FailedLoginException 
	 * @throws ZoneMinderApiNotEnabledException 
	 */
	public  T checkOut() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException {
		T object = null;
		//Just stop, not connected
		if(connected==false)
			return null;
		int iTryCount = 0;
		while ((object==null) && (iTryCount<5)) {
			try {
				if (lock.tryLock(5L, TimeUnit.SECONDS)) {

					object = unlocked.poll(50, TimeUnit.MILLISECONDS);

					if ((object==null) && (iPoolSize<iMaxIdle)) {
						object = create();
					}

				} 

				if (object==null) {
					Thread.sleep(100);
					iTryCount++;

				}
			} catch (InterruptedException e) {
				//We should never end here, unless somebody is trying to shut down
				return null;
			} finally {
				lock.unlock();
			}
		}

		return object;

	}

	/**
	 * Returns object back to the pool.
	 *
	 * @param object object to be returned
	 */
	public void checkIn(T object) {
		if (object == null) {
			return;
		}
		if (this.locked!=null) {
			this.locked.remove(object);
		}
		try {
			if (lock.tryLock(5L, TimeUnit.SECONDS)) {


				this.unlocked.offer(object);
			}
		} catch (InterruptedException e) {
			//We should never end here, unless somebody is trying to shut down
			return;
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Returns object back to the pool.
	 *
	 * @param object object to be returned
	 */
	public void discard(T object) {
		if (object == null) {
			return;
		}

		if (this.locked!=null) {
			iPoolSize--;
			this.locked.remove(object);
		}
	}


	/**
	 * Shutdown this pool.
	 */
	public void shutdown() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	protected T create() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException{
		T result = null;

		try {
			if (lock.tryLock(5L, TimeUnit.SECONDS)) {

				result = onCreate();
				if (result != null)
					iPoolSize++;

			}
		} catch (InterruptedException e) {
			//We should never end here, unless somebody is trying to shut down
			return null;
		}
		finally {
			lock.unlock();
		}

		return result;
	}



	public void setConnected(boolean newState)
	{
		if (newState != connected) {
			if (newState==false) {
				onDisconnected();
			}
			else {
				onConnected();
			}
		}
		connected = newState;
	}


	protected void onConnected()
	{

	}


	protected void onDisconnected()
	{
		if (this.locked!=null) {
			unlocked.clear();
		}
	}

	/**
	 * Creates a new object.
	 *
	 * @return T new object
	 * @throws ZoneMinderUrlNotFoundException 
	 * @throws IOException 
	 * @throws FailedLoginException 
	 * @throws ZoneMinderApiNotEnabledException 
	 * @throws ZoneMinderCredentialsMissingException 
	 */
	protected abstract T onCreate() throws FailedLoginException, IOException, ZoneMinderUrlNotFoundException, ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException;

	protected void initialize(int newSize) throws IOException, ZoneMinderUrlNotFoundException, FailedLoginException, ZoneMinderApiNotEnabledException, ZoneMinderCredentialsMissingException
	{
		if (unlocked==null){
			unlocked = new LinkedBlockingQueue<T>();
		}
		//	    	if (locked==null) {
		//	    		locked = new Hashtable<T, Long>();
		//	    	}
		int expandSize = newSize - unlocked.size();
		for (int i = 0; i < expandSize; i++) {
			unlocked.add(create());
		}

	}
}