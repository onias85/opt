package opt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import opt.exceptions.IncompatibleFormatException;
import opt.exceptions.atomtype.InvalidMatrixException;
import opt.exceptions.atomtype.PropertyIsMissingException;
import opt.utils.Ask;

public class AtomType {

	final Map<String, Object> properties;
	
	
	public AtomType(String index, Map<String, String> properties) {
		//properties.put("INDEX", index);		vc nao pode fazer isso pq a instancia do properties eh imutavel
		//Long  idx     = this.getValidPropertyValue(properties, "INDEX", Format.LONG, Long.class);
		
		HashMap<String, Object> props = new HashMap<>(properties); // ja cria um novo map carregado com os valores do map imodificavel
		props.put("INDEX", index);/// esse map eh alter�vel diferente do properties
		
		Long idx = this.getValidPropertyValue(props, "INDEX", Format.LONG, Long.class);
		Double  c06   = this.getValidPropertyValue(props, "C06", Format.DOUBLE, Double.class);
		String  name  = this.getValidPropertyValue(props, "NAME", Format.STRING, String.class);
		Double  c12_1 = this.getValidPropertyValue(props, "C12_1", Format.DOUBLE, Double.class);
		Double  c12_2 = this.getValidPropertyValue(props, "C12_2", Format.DOUBLE, Double.class);
		Double  c12_3 = this.getValidPropertyValue(props, "C12_3", Format.DOUBLE, Double.class);
		Double  c06NB = this.getValidPropertyValue(props, "C06_NB", Format.DOUBLE, Double.class);
		Double  c12NB = this.getValidPropertyValue(props, "C12_NB", Format.DOUBLE, Double.class);
		boolean CONST = this.getValidPropertyValue(props, "CONST", Format.BOOLEAN, Boolean.class);
		String sequence = this.getValidPropertyValue(props, "MATRIX", Format.STRING, String.class);
		
		
		List<Long> matrix = this.getMatrix(sequence);
		//substituir todos os valores antigos pelas referencias novas
		props.put("C06", c06);
		props.put("INDEX", idx);
		props.put("NAME", name);
		props.put("CONST", CONST);
		props.put("C12_1", c12_1);
		props.put("C12_2", c12_2);
		props.put("C12_3", c12_3);
		props.put("C06_NB", c06NB);
		props.put("C12_NB", c12NB);
		props.put("MATRIX", matrix);
		
		this.properties = Collections.unmodifiableMap(props);
	}

	private List<Long> getMatrix(String sequence) {
		String[] split = sequence.split(" ");
		List<Long> matrix = new ArrayList<>();
		for (String matrixItem : split) {
			if(matrixItem.isEmpty()) {
				continue;
			}
			boolean isNotNumber = false == Ask.isLong(matrixItem);
			if(isNotNumber) {
				throw new InvalidMatrixException(sequence);
			}
			matrix.add(new Long(matrixItem));
		}
		return matrix;
	}

	@SuppressWarnings("unchecked")
	public <P> P getValidPropertyValue(Map<String, Object> properties, String propertyName, Format format, Class<P> clazz) {
		
		boolean thisPropertyIsNotPresent = false == properties.containsKey(propertyName);
		
		if(thisPropertyIsNotPresent) {
			throw new PropertyIsMissingException(propertyName, this.properties);
		}
		
		Object object = properties.get(propertyName);

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
		return (List<Long>) this.properties.get("MATRIX");
	}
	

	public Long getIndex() {
		return (Long) this.properties.get("IDX");
	}
	
	public boolean isConst() {
		return (boolean) this.properties.get("CONST");
	}
	
	
	public String getName() {
		return this.properties.get("NAME").toString();
	}
	
	public Double getC06() {
		return (Double) this.properties.get("C06");
	}
	
	public Double getC121() {
		return (Double) this.properties.get("C12_1");
	}
	
	public Double getC122() {
		return (Double) this.properties.get("C12_2");
	}
	
	public Double getC123() {
		return (Double) this.properties.get("C12_3");
	}
	
	public Double getC06NB() {
		return (Double) this.properties.get("C06_NB");
	}
	

	public Double getC12NB() {
		return (Double) this.properties.get("C12_NB");
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