package opt.lj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opt.Format;
import opt.ParameterDescription;
import opt.ParametersSet;

public class AtomType {
	
	private final ParametersSet ps;
	
	@SuppressWarnings("unchecked")
	public AtomType(String index, Map<String, Object> properties) {
		
		
		this.ps = new ParametersSet(properties, index, 
				new ParameterDescription<Object, List<Long>>("MATRIX", Format.STRING, properties, new MatrixTransformation(properties))
				,new ParameterDescription<Object, Object>("C06_NB", Format.DOUBLE, properties)
				,new ParameterDescription<Object, Object>("CONST", Format.BOOLEAN, properties)
				,new ParameterDescription<Object, Object>("C12_NB", Format.DOUBLE, properties)
				,new ParameterDescription<Object, Object>("C12_1", Format.DOUBLE, properties)
				,new ParameterDescription<Object, Object>("C12_2", Format.DOUBLE, properties)
				,new ParameterDescription<Object, Object>("C12_3", Format.DOUBLE, properties)
				,new ParameterDescription<Object, Object>("NAME", Format.STRING, properties)
				,new ParameterDescription<Object, Object>("C06", Format.DOUBLE, properties)
				);
				
	}

	public AtomType(Map<String, String> map, String keyValue) {

		this(keyValue, get(map));
	}
	
	private static Map<String, Object> get(Map<String, String> map){
		Set<String> keySet = map.keySet();
		Map<String, Object> result = new HashMap<>();
		for (String key : keySet) {
			Object value = map.get(key);
			result.put(key, value);
		}
		return result;
	}

	public ParametersSet getParametersSet() {
		return this.ps;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getMatrix(){
	
		Object object = this.ps.properties.get("MATRIX");
		List<Long> list = (List<Long>)object;
		return list;
	}
	
	public Long getIndex() {
		Object object2 = this.ps.properties.get("INDEX");
		return new Long(object2.toString());
	}
}