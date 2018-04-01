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
		
		//properties.put("INDEX", index);		vc nao pode fazer isso pq a instancia do properties eh imutavel
		//Long  idx     = this.getValidPropertyValue(properties, "INDEX", Format.LONG, Long.class);
		
//		HashMap<String, Object> props = new HashMap<>(properties); // ja cria um novo map carregado com os valores do map imodificavel
//		props.put("INDEX", index);/// esse map eh alter�vel diferente do properties
//		
//		boolean CONST   =  this.getValidPropertyValue(props, "CONST", Format.BOOLEAN, Boolean.class);
//		Double  c06NB   =  this.getValidPropertyValue(props, "C06_NB", Format.DOUBLE, Double.class);
//		Double  c12NB   =  this.getValidPropertyValue(props, "C12_NB", Format.DOUBLE, Double.class);
//		String sequence =  this.getValidPropertyValue(props, "MATRIX", Format.STRING, String.class);
//		Double  c12_1   =  this.getValidPropertyValue(props, "C12_1", Format.DOUBLE, Double.class);
//		Double  c12_2   =  this.getValidPropertyValue(props, "C12_2", Format.DOUBLE, Double.class);
//		Double  c12_3   =  this.getValidPropertyValue(props, "C12_3", Format.DOUBLE, Double.class);
//		String  name    =  this.getValidPropertyValue(props, "NAME", Format.STRING, String.class);
//		Double  c06     =  this.getValidPropertyValue(props, "C06", Format.DOUBLE, Double.class);
//		Long    idx     =  this.getValidPropertyValue(props, "INDEX", Format.LONG, Long.class);
//		
//		
//		List<Long> matrix = this.getMatrix(sequence);
//		//substituir todos os valores antigos pelas referencias novas
//		props.put("MATRIX", matrix);
//		props.put("C06_NB", c06NB);
//		props.put("C12_NB", c12NB);
//		props.put("CONST", CONST);
//		props.put("C12_1", c12_1);
//		props.put("C12_2", c12_2);
//		props.put("C12_3", c12_3);
//		props.put("INDEX", idx);
//		props.put("NAME", name);
//		props.put("C06", c06);
//		
//		this.properties = Collections.unmodifiableMap(props);
		
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