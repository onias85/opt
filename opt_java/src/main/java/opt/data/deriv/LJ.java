package opt.data.deriv;

import opt.data.Compound;
import opt.data.Property;
import opt.lj.AtomType;

public class LJ extends Deriv {
	
	public final AtomType at1;

	public final AtomType at2;
	
	
	public LJ(double parameterValue, double derivValue, Property property, Compound compound, String name, AtomType at1, AtomType at2) {
		super(parameterValue, derivValue, property, compound, name);
		this.at1 = at1;
		this.at2 = at2;
	}

	public boolean isC06() {
		return false;
	}
	
	

}
