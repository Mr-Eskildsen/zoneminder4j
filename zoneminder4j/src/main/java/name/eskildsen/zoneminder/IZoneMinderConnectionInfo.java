package name.eskildsen.zoneminder;

import java.net.MalformedURLException;
import java.net.URI;

import name.eskildsen.zoneminder.general.ProtocolType;

public interface IZoneMinderConnectionInfo {
    public String getProtocolName();
    public ProtocolType getProtocolType();
    public String getHostName();
    public Integer getHttpPort();
    public Integer getTelnetPort();
    public String getUserName();
    public String getPassword();
    public String getZoneMinderBasePath();

    public URI getZoneMinderRootUri() throws MalformedURLException;
	public Integer getTimeout();

}
