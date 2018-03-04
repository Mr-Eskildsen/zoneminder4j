package name.eskildsen.zoneminder.common;

public enum HashAuthenticationEnum {
	None("none"),
	Plain("plain"),
	Hashed("hashed");

	private String name = "";
	
	private HashAuthenticationEnum(String name) {
		this.name = name;
	}
	public String toString() {
	    return name; // name().charAt(0) + name().substring(1).toLowerCase();
	}
	
	public static HashAuthenticationEnum getEnum(String value) {
	    for(HashAuthenticationEnum v : values())
	        if(v.toString().equalsIgnoreCase(value)) return v;
	    throw new IllegalArgumentException();
	}   
	
	
	
}
