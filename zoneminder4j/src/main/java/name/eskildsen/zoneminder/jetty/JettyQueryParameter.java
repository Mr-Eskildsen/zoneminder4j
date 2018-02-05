package name.eskildsen.zoneminder.jetty;

public class JettyQueryParameter {

	private String name;
	private String value;
	
	public JettyQueryParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {return name;}
	public String getValue() {return value;}
}
