package name.eskildsen.zoneminder.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.FailedLoginException;

import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderEventManager;
import name.eskildsen.zoneminder.IZoneMinderEventSubscriber;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfig;
import name.eskildsen.zoneminder.api.config.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.trigger.ZoneMinderEventNotifier;

public class ZoneMinderEventManager extends ZoneMinderEventNotifier  implements IZoneMinderEventManager {

	//create an object of SingleObject
	private static ZoneMinderEventManager instance = new ZoneMinderEventManager();
	private ZoneMinderConnectionInfo connection = null;
	private boolean _connected = false;

	

	//make the constructor private so that this class cannot be
	//instantiated
	private ZoneMinderEventManager()
	{
	}
	
	
	//Get the only interface
	public static IZoneMinderEventManager getInstance(){
	   return instance;
	}

	
	
	@Override
	public boolean isConnected() {
		boolean connected = false;
		
		if (connection!=null) {
			synchronized(connection) {
				connected = _connected;
			}
		}
		return connected;
	}
	
	public boolean validateConnection(IZoneMinderConnectionInfo connection)
	{
		return verifyTelnetConnection((ZoneMinderConnectionInfo)connection) && verifyHttpConnection((ZoneMinderConnectionInfo)connection);
		
	}


	protected boolean Initialize(IZoneMinderConnectionInfo conn) throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
		   
			
			if (verifyHttpConnection((ZoneMinderConnectionInfo)conn) && verifyTelnetConnection((ZoneMinderConnectionInfo)conn))
			{
				connection = (ZoneMinderConnectionInfo)conn;
				setConnected(true);
		   }
		   else  {
			   setConnected(false);
		   }
		   
		   return true;
	   }
	
	
	public boolean UpdateConnection(IZoneMinderConnectionInfo connection)
			throws GeneralSecurityException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
		
		return Initialize(connection);
	}

	   
	   
	   protected boolean verifyTelnetConnection(ZoneMinderConnectionInfo connection) {
			try {
				ZoneMinderSession session = new ZoneMinderSession(connection, false, true);
				   return session.isConnectedTelnet();
			} catch (FailedLoginException | IllegalArgumentException | IOException | ZoneMinderUrlNotFoundException e) {
				return false;
			}

	   }
	   
	   
	   
	   protected boolean verifyHttpConnection(ZoneMinderConnectionInfo connection) {
		   try {
			   ZoneMinderSession session = new ZoneMinderSession(connection, true, false);
			   return session.isConnectedHttp();
			} catch (FailedLoginException | IllegalArgumentException | IOException | ZoneMinderUrlNotFoundException e) {
				return false;
			}

	   }
	   
   
	   public void setConnected(boolean newState) {
		   if (connection==null) {
			   _connected = false;
			   return;
		   }
		   
		   synchronized(connection) {
			   _connected = newState;
		   }
	}
	   

		private Thread tcpListener = null;
		private TCPListener tcpListenerRunnable = null;
		private static final int TELNET_TIMEOUT = 1000;


		
	@Override
	protected synchronized  boolean startTelnetListener(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, GeneralSecurityException, IOException, ZoneMinderUrlNotFoundException {
		
		Initialize(connection);

		tcpListenerRunnable = new TCPListener((ZoneMinderConnectionInfo) connection);
        tcpListener = new Thread(tcpListenerRunnable);
        tcpListener.setPriority(Thread.MAX_PRIORITY);
        tcpListener.start();
	
    	return true;
	
	    
	}

	   
	@Override
	protected synchronized boolean stopTelnetListener() {
		if (tcpListenerRunnable != null) {
			tcpListenerRunnable.stop(); 
		}
		if (tcpListener != null) {
			tcpListener.interrupt();
		}


		return true;
	}

	    
	    
	    /**
	     * TCPMessageListener: Receives Socket messages from the ZoneMinder API.
	     */
	   
	    private class TCPListener implements Runnable {
	    	private ZoneMinderSession sessionTelnet = null;
	    	private ZoneMinderConnectionInfo connection = null;
	        
	    	private boolean _allowRun = true;

			public TCPListener (ZoneMinderConnectionInfo connection) {
	    		
	    		this.connection = connection;
	    		 
	    	}
	    	
	    	private void connect() throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
	    		sessionTelnet = new ZoneMinderSession(connection, false, true );
	    	}
	    	
	    	protected synchronized boolean allowRun() {
	    		return _allowRun;
	    	}

	    	
	    	public synchronized void stop() 
	    	{
	    		_allowRun = false;
	    	
	    		try {
					sessionTelnet.disconnectTelnet();
				} catch (IOException e) {
				}
	    	}
	    	
	    	// Run method. Runs the MessageListener thread
         
	        @Override
	        public void run() {
	            String messageLine = "";

                while(allowRun()) {
                	try {
	                
		                if (sessionTelnet==null) {
								connect();
		                }
		                while ((sessionTelnet!=null) && (allowRun())) {
		                	messageLine = sessionTelnet.pollTelnet();
		                	if (messageLine == null) {
		                		sessionTelnet = null;
		                		connect();
		                	}
		                	else if (messageLine != "") {
		                            ZoneMinderTriggerEvent event = new ZoneMinderTriggerEvent( messageLine );
		                        	tripMonitor(event);
		                                                 
		                    }
		                	Thread.yield();
		                }
		            
		        	//Don't expect any of these exceptions (Telnet son't fail login
		            } catch (FailedLoginException | IllegalArgumentException 
						| ZoneMinderUrlNotFoundException e) {
		        	
		        	} catch(IOException ioe) {
		        		sessionTelnet = null;
		        		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
		        	}
                	
	            }
	        }	   
	    }



		@Override
		public boolean validateLogin(IZoneMinderConnectionInfo connection) throws IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
			   try {
				   ZoneMinderSession session = new ZoneMinderSession(connection, true, false);
				   return session.isConnectedHttp();
				} catch (FailedLoginException e) {
					return false;
				}
			   
			
		}

		@Override
		public boolean isApiEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
			   try {
				   ZoneMinderSession session = null;
					session = new ZoneMinderSession(connection, true, false);
					ZoneMinderServerProxy server = new ZoneMinderServerProxy(session);
					return server.isApiEnabled();

				} catch (NullPointerException e) {
					return false;
				}
		}

		@Override
		public boolean isTriggerOptionEnabled(IZoneMinderConnectionInfo connection) throws FailedLoginException, IllegalArgumentException, IOException, ZoneMinderUrlNotFoundException {
			   try {
				   ZoneMinderSession session = null;
					session = new ZoneMinderSession(connection, true, false);
					ZoneMinderServerProxy server = new ZoneMinderServerProxy(session);
					return server.isTriggerOptionEnabled();
				} catch (NullPointerException e) {
					return false;
				}
		}


}
