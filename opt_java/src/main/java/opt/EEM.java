package opt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import opt.data.Compound;
import opt.readers.Argument;

public class EEM {
	
	
	public final Map<String, Object> compounds;

	public final Map<String, Object> atomsType;

	public EEM(Map<String, Object> compounds, Map<String, Object> atomsType) {

		this.compounds = Collections.unmodifiableMap(compounds);
		this.atomsType = Collections.unmodifiableMap(atomsType);
	}
	

	public EEM computeCharge(){
		Map<String, Compound> compounds = this.getMap(this.compounds, Compound.class);
		Set<String> compoundNames = this.compounds.keySet();
		
		
		compoundNames.stream().forEach(compoundName -> {
			System.out.println("********************************** " + compoundName);
			Compound compound = compounds.get(compoundName);
			Map<String, Object> valuesFromFile = Argument.LJ.getValuesFromFile();
//			System.out.println(valuesFromFile.keySet());

			compound.computeCharge(valuesFromFile);
		});
		
		EEM eem = new EEM(this.compounds, this.atomsType);
		return eem;
	}
	

	@SuppressWarnings("unchecked")
	private <T>Map<String, T> getMap(Map<String, Object> map, Class<T> clazz){

		Map<String, T> mapa = new HashMap<>();
		Set<String> keySet = map.keySet();
		keySet.stream().forEach(key -> {
			Object object = map.get(key);
			T t = (T)object;
			mapa.put(key, t);
		});
		
		return mapa;
	} 
	
	public static void main(String[] args) {
		Argument.DATA.read("data.txt");
		Map<String, Object> compounds = Argument.DATA.getValuesFromFile();
		Argument.ATOM.read("listAtom.dat");
		Argument.LJ.read("lj.txt");
		Map<String, Object> atomsType = Argument.LJ.getValuesFromFile();
		Argument.ANGLETYPE.read("angleType.dat");
		Argument.BONDTYPE.read("bondType.dat");
		Argument.BOND.read("listBond.dat");
		Argument.ANGLE.read("listAng.dat");
		
	
		Map<String, Object> valuesFromFile = Argument.LJ.getValuesFromFile();

		EEM obj = new EEM(compounds, atomsType);
	
		obj.computeCharge();
	}
	
	
}
