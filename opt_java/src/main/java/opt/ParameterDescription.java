package opt;
import java.util.Collections;
import java.util.Map;

import com.google.gson.GsonBuilder;

import opt.exceptions.IncompatibleFormatException;
import opt.exceptions.atomtype.PropertyIsMissingException;

public class ParameterDescription<Input, Output> {

	
	public final ParameterTransformation<Output> transformation;

	public final Map<String, Object> properties;
	
	public final Format format;
	
	public final String name;
	
	
	public ParameterDescription(String name, Format format, Map<String, Object> properties) {

		this.transformation = new DefaultTransformation<>(name, format, properties); 
	
		this.properties = properties;
		
		this.format = format;
		
		this.name = name;
	}
	
	
	public ParameterDescription(String name, Format format, Map<String, Object> properties, ParameterTransformation<Output> transformation) {

		this.transformation = transformation; 
		
		this.properties = properties;
		
		this.format = format;

		this.name = name;
	}
	
	
	
	public Output getTransformedValue(Input input) {
		
		boolean isDefaultTransformation = this.transformation instanceof DefaultTransformation;
		
		if(isDefaultTransformation) {
			Output transformedValue = this.transformation.getTransformedValue();
			return transformedValue;
		}
		
		DefaultTransformation<Input, Output> dt = new DefaultTransformation<>(this.name, this.format, this.properties);
		dt.getTransformedValue();
		Output tv = this.transformation.getTransformedValue();
		return tv;
	}
	


	@Override
	public String toString() {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
		return json;
	}

	static class DefaultTransformation<Input, Output> implements ParameterTransformation<Output>{

		public final String name;

		public final Format format;
		
		public final Map<String, Object> properties;
		
		
		public DefaultTransformation(String name, Format format, Map<String, Object> properties) {

			this.properties = Collections.unmodifiableMap(properties);
			this.format = format;
			this.name = name;
		}

		@SuppressWarnings("unchecked")
		private Output getValidPropertyValue(Map<String, Object> properties) {
			
			boolean thisPropertyIsNotPresent = false == properties.containsKey(this.name);
			
			if(thisPropertyIsNotPresent) {
				throw new PropertyIsMissingException(this.name, properties);
			}
			
			Object object = properties.get(this.name);

			if(object == null) {
				throw new PropertyIsMissingException(this.name, properties);
			}
			
			String string = object.toString();
			if(string == null) {
				throw new PropertyIsMissingException(this.name, properties);
			}

			boolean empty = string.trim().isEmpty();
			if(empty) {
				throw new PropertyIsMissingException(this.name, properties);
			}
			
			String str = string;
			
			boolean isNotCompatible = false == this.format.isCompatible(str);
			
			if(isNotCompatible) {
				throw new IncompatibleFormatException(this.name, this.format);
			}
			
			Object parse = this.format.parse(str);
			
			return (Output)parse;
		}

		@Override
		public Output getTransformedValue() {
			Output validPropertyValue = this.getValidPropertyValue(this.properties);
			return validPropertyValue;
		}
		
	}
}
