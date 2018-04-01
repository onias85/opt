package opt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opt.exceptions.IncompatibleFormatException;

public class ParametersSet {

	
	public final Map<String, Object> properties;

	@SuppressWarnings("unchecked")
	public ParametersSet(Map<String, ?> properties, String indexIdentification, ParameterDescription<Object, ?>...descriptions) {

		Map<String, Object> props = this.getProperties(indexIdentification);
		
		for (ParameterDescription<Object, ?> desc : descriptions) {
			Object object = properties.get(desc.name);
			Object validPropertyValue = desc.getTransformedValue(object);
			props.put(desc.name, validPropertyValue);
		}
		
		this.properties = Collections.unmodifiableMap(props);
	}

	private Map<String, Object> getProperties(String indexIdentification) {
		Map<String, Object> props = new HashMap<>();
		
		boolean isNotCompatible = false == Format.LONG.isCompatible(indexIdentification);
		
		if(isNotCompatible) {
			throw new IncompatibleFormatException("INDEX", Format.LONG);
		}

		props.put("INDEX", indexIdentification);
		return props;
	}





}
