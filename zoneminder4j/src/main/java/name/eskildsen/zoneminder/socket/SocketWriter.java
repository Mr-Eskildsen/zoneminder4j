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

public class SocketWriter extends SocketCore {

	ReentrantLock writeLock;
	
	public SocketWriter(String host, int port) throws IOException {
		super(host, port);
		writeLock = new ReentrantLock();
	}
	
	
	/*******************************************************
	 * 
	 *	TCP
	 * 
	 *******************************************************/
	/**
	 *	Telnet connection 
	 */
	private PrintWriter telnetOutput = null;
//	private BufferedReader telnetInput = null;
	
	
	private static final int TELNET_TIMEOUT = 1000;
	
	@Override
	protected boolean onConnected() throws IOException {
	
		telnetOutput = new PrintWriter(getOutputStream(), true);
		return true;
	}
	
	@Override
	protected void onDisconnected() throws IOException
	{
		if (telnetOutput!=null) {
			telnetOutput.close();
			telnetOutput = null;
		}

	}
	
	public boolean write(String writeString) throws IOException {
	
		writeLock.lock();
		try {
		if (!isConnected())
			connect();
	
		if (!isConnected())
			return false;
	
		telnetOutput.write(writeString);
		telnetOutput.flush();
		
		disconnect();
		}
		finally {
			writeLock.unlock();
		}
		return true;
	
	}
}
