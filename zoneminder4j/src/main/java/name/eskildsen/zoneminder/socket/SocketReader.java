package name.eskildsen.zoneminder.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class SocketReader extends SocketCore {

	ReentrantLock readLock;
	
	public SocketReader(String host, int port) throws IOException {
		super(host, port);
		readLock = new ReentrantLock();
	}
	
	
	/*******************************************************
	 * 
	 *	TCP
	 * 
	 *******************************************************/
	/**
	 *	Telnet connection 
	 */
//	private PrintWriter telnetOutput = null;
	private BufferedReader telnetInput = null;
	
	
	private static final int TELNET_TIMEOUT = 1000;
	
	@Override
	protected boolean onConnected() throws IOException {
	
		telnetInput = new BufferedReader(new InputStreamReader(getInputStream()));
		return true;
	}
	
	@Override
	protected void onDisconnected() throws IOException
	{
		if (telnetInput!=null) {
			telnetInput.close();
			telnetInput = null;
		}
	
	}
	
	
	
	/**
	 * @throws IOException 
	 *
	 **/
	public String poll() throws IOException  {
		String message = "";
		//if(readLock.isLocked() && !readLock.isHeldByCurrentThread()) {
		if(readLock.isLocked()) {
			throw new IOException("Could not obtain lock to read socket");
		}

		readLock.lock();
		try {
			if (!isConnected())
				connect();
	
			if (!isConnected())
				return "";
	
			message = telnetInput.readLine();
	
		} catch (SocketTimeoutException stException) {
			// We known it is comnming, so just ignore this
		}
		finally {
			readLock.unlock();
		}
	
	
		return message;
	}
	
	
}
