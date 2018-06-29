package opt.data.deriv;

import opt.data.Compound;
import opt.data.Property;

public class Charge extends Deriv {
	
	public final long indexInCompound;

	public Charge(double parameterValue, double derivValue, Property property, Compound compound, String name,
			long indexInCompound) {
		super(parameterValue, derivValue, property, compound, name);
		this.indexInCompound = indexInCompound;
	}

	public boolean isC06() {
		return false;
	}
}
