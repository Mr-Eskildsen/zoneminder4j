package name.eskildsen.zoneminder.event;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.FailedLoginException;

import org.jsoup.Connection;

import name.eskildsen.zoneminder.IZoneMinderConnectionHandler;
import name.eskildsen.zoneminder.IZoneMinderConnectionInfo;
import name.eskildsen.zoneminder.IZoneMinderEventSession;
import name.eskildsen.zoneminder.IZoneMinderEventSubscriber;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTelnetRequest;
import name.eskildsen.zoneminder.api.telnet.ZoneMinderTriggerEvent;
import name.eskildsen.zoneminder.common.ZoneMinderConfigEnum;
import name.eskildsen.zoneminder.data.ZoneMinderConfigImpl;
import name.eskildsen.zoneminder.exception.ZoneMinderApiNotEnabledException;
import name.eskildsen.zoneminder.exception.ZoneMinderAuthenticationException;
import name.eskildsen.zoneminder.exception.ZoneMinderUrlNotFoundException;
import name.eskildsen.zoneminder.socket.SocketReader;
import name.eskildsen.zoneminder.socket.SocketWriter;

public class ZoneMinderEventManager extends ZoneMinderEventNotifier  implements IZoneMinderEventSession {

	//create an object of SingleObject
	////private static ZoneMinderEventManager instance = new ZoneMinderEventManager();
	//private ZoneMinderConnectionInfo _connection = null;
	
	//private boolean _connected = false;
	private SocketWriter socketWriter = null;
	
	public ZoneMinderEventManager()
	{
	}
	
	public ZoneMinderEventManager(IZoneMinderConnectionHandler conn)
	{
		initialize(conn);
	}
	
	@Override
	public boolean isConnected() {
		if (tcpListenerRunnable==null)
			return false;
		
		return tcpListenerRunnable.isConnected();
	}
	

	protected boolean initialize(IZoneMinderConnectionHandler conn) 
	{
		try {
			
			if (socketWriter==null) {
				socketWriter = new SocketWriter(conn.getHostName(), conn.getTelnetPort());
			}
			return socketWriter.isConnected();
		} catch (IllegalArgumentException | IOException e) {
		}
		return false;	   }
	
	
	//TODO - Remove this?
	public boolean UpdateConnection(IZoneMinderConnectionHandler connection)
	{
		return initialize(connection);
	}

	   
	
    /** *****************************************************
     * 
     * Event Trigger
     * @throws IOException 
     * @throws ZoneMinderUrlNotFoundException 
     * @throws FailedLoginException 
     * 
      ***************************************************** */
	@Override
	public void activateForceAlarm(String monitorId, Integer priority, String reason,
            String note, String showText, Integer timeoutSeconds) throws IOException
	{
	
		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(ZoneMinderEventAction.ON, monitorId, priority, reason,
	            note, showText, timeoutSeconds);
		socketWriter.write(request.toCommandString());
		
	}

	@Override
	public void deactivateForceAlarm(String monitorId) throws IOException
	{
		ZoneMinderTelnetRequest request = new ZoneMinderTelnetRequest(ZoneMinderEventAction.OFF, monitorId, 255, "",
	            "", "", 0);
		if (socketWriter==null) {
			throw new IOException("Write to socket failed. Socket connection not initialised");
		}
		socketWriter.write(request.toCommandString());
	}
	
	
	private Thread tcpListener = null;
	private TCPListener tcpListenerRunnable = null;
	private static final int TELNET_TIMEOUT = 1000;


		
	@Override
	protected synchronized  boolean startListener(IZoneMinderConnectionHandler connection) {

		tcpListenerRunnable = new TCPListener(connection.getHostName(), connection.getTelnetPort());
        tcpListener = new Thread(tcpListenerRunnable);
        tcpListener.setPriority(Thread.MAX_PRIORITY);
        tcpListener.start();
	
    	return true;
	
	    
	}

	   
	@Override
	public synchronized boolean stopListener() {
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
		private SocketReader socketReader = null;
		private String host;
		private int port;
    	private boolean _allowRun = true;

		public TCPListener (String host, int port) {
    		this.host = host;
    		this.port = port;
		}
	    	
    	private void connect() throws IOException {
    		
    		socketReader = new SocketReader(host, port);
		}
	    	
    	protected synchronized boolean allowRun() {
    		return _allowRun;
    	}

	    
    	public synchronized  boolean isConnected()
    	{
    		if (socketReader!=null) {
    			return false;
    		}
    		return socketReader.isConnected();
    	}
    	
    	public synchronized void stop() 
    	{
    		_allowRun = false;
    	
    		try {
    			socketReader.disconnect();
			} catch (IOException e) {
			}
    	}
    	
    	// Run method. Runs the MessageListener thread
     
        @Override
        public void run() {
            String messageLine = "";

            while(allowRun()) 
            {
            	try {
                
	                if (socketReader==null) {
							connect();
	                }
	                while ((socketReader!=null) && (allowRun())) {
	                	messageLine = socketReader.poll();
	                	if (messageLine == null) {
	                		socketReader = null;
	                		connect();
	                	}
	                	else if (messageLine != "") {
	                            ZoneMinderTriggerEvent event = new ZoneMinderTriggerEvent( messageLine );
	                        	tripMonitor(event);
	                                                 
	                    }
	                	Thread.yield();
	                }
	            
	        	//Don't expect any of these exceptions (Telnet won't fail login :-)
	            } catch (IllegalArgumentException e) {
	        	
	        	} catch(IOException ioe) {
	        		socketReader = null;
	        		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
            	
	        	}
            	catch(Exception ex) {
            		//Left blank
            	}
            }
        }	   
    }





}
