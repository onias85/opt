package opt.data.deriv;

import opt.data.Compound;
import opt.data.Property;
import opt.lj.AtomType;

public class Charge extends Deriv {
	
	public final AtomType at1;

	
	public Charge(double parameterValue, double derivValue, Property property, Compound compound, String name, 	AtomType at1) {
		super(parameterValue, derivValue, property, compound, name);
		this.at1 = at1;
	}







	public boolean isC06() {
		return false;
	}
	
	

}
