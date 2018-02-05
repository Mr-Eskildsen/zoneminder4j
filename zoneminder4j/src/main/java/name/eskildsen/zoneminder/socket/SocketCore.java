package name.eskildsen.zoneminder.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public abstract class SocketCore {

	private String host = null;
	private int port = -1; 
	
	public SocketCore(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
	}
	
	protected String getHost() {
		return host;
	}
	protected int getPort() {
		return port; 	
	}

	/*******************************************************
	 * 
	 *	Overrides
	 * @throws IOException 
	 * 
	 *******************************************************/
	protected abstract boolean onConnected() throws IOException;
	protected abstract void onDisconnected() throws IOException;
	
	
	
	protected InputStream getInputStream() throws IOException
	{
		if (telnetSocket==null)
			return null;
		return telnetSocket.getInputStream();
	}
	
	protected OutputStream getOutputStream() throws IOException
	{
		if (telnetSocket==null)
			return null;
		return telnetSocket.getOutputStream();
	}
	
	
	/*******************************************************
	 * 
	 *	TCP
	 * 
	 *******************************************************/
	/**
	 *	Telnet connection 
	 */
	private Socket telnetSocket = null;

	
	private static final int TELNET_TIMEOUT = 1000;
	
	public boolean isConnected()
	{
		if (telnetSocket==null)
			return false;
		return telnetSocket.isConnected();
	}
	
	
	public boolean connect() throws IOException {
	
		telnetSocket = new Socket();
		SocketAddress TPIsocketAddress = new InetSocketAddress(getHost(), getPort());
		telnetSocket.connect(TPIsocketAddress, TELNET_TIMEOUT);
		
		if (telnetSocket.isConnected())
		{
			return onConnected();
		}
		return telnetSocket.isConnected();
	}
	
	public void disconnect() throws IOException
	{
		onDisconnected();
		
		if (telnetSocket!=null) {
			telnetSocket.close();
			telnetSocket = null;
		}
	}
	

	
	
	
}
