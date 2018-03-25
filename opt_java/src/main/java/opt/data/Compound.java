package opt.data;

import java.util.Collections;
import java.util.List;

public class Compound {

	public final String name;
	
	public final Boolean constant;
	
	public final List<Property> properties;

	public Compound(String name, Boolean constant, List<Property> properties) {

		
		this.properties = Collections.unmodifiableList(properties);
		this.constant = constant;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Compound [name=" + name + ", constant=" + constant + ", properties=" + properties + "]";
	}
	
	
	
	
}
