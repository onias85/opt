package opt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import opt.exceptions.IncompatibleFormatException;
import opt.exceptions.InvalidMatrixException;
import opt.exceptions.PropertyIsMissingException;
import opt.utils.Ask;

public class AtomType {

	final Map<String, Object> properties;
	
	
	public AtomType(String index, Map<String, String> properties) {
		
		Map<String, Object> map = new HashMap<>();
		map.putAll(properties);
		map.put("index", index);

		this.properties = map;
	}

	@SuppressWarnings("unchecked")
	public <P> P getValidPropertyValue(String propertyName, Format format, Class<P> clazz) {
		
		boolean thisPropertyIsNotPresent = false == this.properties.containsKey(propertyName);		
		
		if(thisPropertyIsNotPresent) {
			throw new PropertyIsMissingException(propertyName, this.properties);
		}
		
		Object object = this.properties.get(propertyName);

		if(object == null) {
			throw new PropertyIsMissingException(propertyName, this.properties);
		}
		
		String string = object.toString();
		if(string == null) {
			throw new PropertyIsMissingException(propertyName, this.properties);
		}

		boolean empty = string.trim().isEmpty();
		if(empty) {
			throw new PropertyIsMissingException(propertyName, this.properties);
		}
		
		String str = string;
		
		boolean isNotCompatible = false == format.isCompatible(str);
		
		if(isNotCompatible) {
			throw new IncompatibleFormatException(propertyName, clazz);
		}
		
		Object parse = format.parse(str);
		
		return (P)parse;
		
	}
	
	public List<Long> getMatrix() {
		
		String matrix = this.getValidPropertyValue("MATRIX", Format.STRING, String.class);
		String[] split = matrix.split(" ");
		List<Long> result = new ArrayList<>();
		
		for (String matrixItem : split) {
			
			if(matrixItem.isEmpty()) {
				continue;
			}
			
			boolean isNotNumber = false == Ask.isLong(matrixItem);
			if(isNotNumber) {
				throw new InvalidMatrixException(matrix);
			}
			
			result.add(new Long(matrixItem));
		}
		return result;
	}
	

	public Long getIndex() {
		Long validPropertyValue = this.getValidPropertyValue("index", Format.LONG, Long.class);
		return validPropertyValue;
	}
	
	public boolean isConst() {
		Boolean validPropertyValue = this.getValidPropertyValue("CONST", Format.BOOLEAN, Boolean.class);
		return validPropertyValue;
	}
	
	
	public String getName() {
		String validPropertyValue = this.getValidPropertyValue("NAME", Format.STRING, String.class);
		return validPropertyValue;
	}
	
	public Double getC06() {
		Double validPropertyValue = this.getValidPropertyValue("C06", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	
	public Double getC121() {
		Double validPropertyValue = this.getValidPropertyValue("C12_1", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	
	public Double getC122() {
		Double validPropertyValue = this.getValidPropertyValue("C12_2", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	
	public Double getC123() {
		Double validPropertyValue = this.getValidPropertyValue("C12_3", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	
	public Double getC06NB() {
		Double validPropertyValue = this.getValidPropertyValue("C06_NB", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	

	public Double getC12NB() {
		Double validPropertyValue = this.getValidPropertyValue("C12_NB", Format.DOUBLE, Double.class);
		return validPropertyValue;
	}
	

	
	@Override
	public String toString() {

		String json = new GsonBuilder().setPrettyPrinting().create().toJson(this.properties);
		return json;
	}
	
}



enum Format{
	
	DOUBLE {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isDouble(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {

			return new Double(str);
		}
	}, LONG {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isLong(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {

			return new Long(str);
		}
	}, BOOLEAN {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isBoolean(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {
			if("true".equalsIgnoreCase(str)) {
				return true;
			}
			return false;
		}
	}, STRING {
		@Override
		boolean isCompatible(String str) {
			return true;
		}

		@Override
		Object parse(String str) {

			return str;
		}
	};
	
	abstract boolean isCompatible(String str);
	abstract Object parse(String str);
}