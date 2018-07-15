package opt.data.atom;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class Atom {

	public final String code;

	public final int index;

	public final String name;

	public final int type;

	public Double charge;

	public final Map<Integer, Double> firstNeighbour = new HashMap<Integer, Double>();

	public final Map<Integer, Double> secondNeighbour = new HashMap<Integer, Double>();

	public Atom(String code, int index, String name, int type, Double charge) {

		this.charge = charge;
		this.index = index;
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public void addFirstNeighbour(int index, double value) {
		this.firstNeighbour.put(index, value);
	}

	@Override
	public String toString() {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
		return json;
	}

	public double getDistance(int index2) {

		boolean isFirstNeighbour = this.firstNeighbour.containsKey(index2);

		if(isFirstNeighbour) {
			Double double1 = this.firstNeighbour.get(index2);
			
			
			
			return double1;
			
		}
		boolean isSecondNeighbour = this.secondNeighbour.containsKey(index2);

		if(isSecondNeighbour) {
			Double double1 = this.secondNeighbour.get(index2);
			
			
			return double1;
			
		}
		throw new RuntimeException("Index out range " + index2);
		
	}

	public void addSecondNeighbour(int index, double value) {
		this.firstNeighbour.put(index, value);
		
	}
	
	public Atom changeCharge(double charge) {
		
		Atom atom = new Atom(this.code, this.index, this.name, this.type, charge);
		return atom;
		
	} 
	
	
}
