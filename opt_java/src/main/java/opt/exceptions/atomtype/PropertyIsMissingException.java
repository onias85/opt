package opt.exceptions.atomtype;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class PropertyIsMissingException extends RuntimeException{

	public PropertyIsMissingException(String propertyName, Map<String, Object> properties) {
		super(getErrorMessage(propertyName, properties));
	}

	private static String getErrorMessage(String propertyName, Map<String, Object> properties) {
		
		GsonBuilder gb = new GsonBuilder();
		
		Gson gson = gb.setPrettyPrinting().create();
		
		String json = gson.toJson(properties);
		
		String format = String.format("The property named '%s' was not found in the map "+ json, propertyName);
		
		return format;
	}
	
}
