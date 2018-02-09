package name.eskildsen.zoneminder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in method only.
public @interface ZoneMinderJson {
	//Name of variable in ZoneMinder JSON 
	public String Name();

}
