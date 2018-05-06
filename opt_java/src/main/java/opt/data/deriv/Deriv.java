package opt.data.deriv;

import opt.data.Compound;
import opt.data.Property;

public abstract class Deriv {

	// composto, propriedade, valor do parametro, valor da derivada
	
	public final double parameterValue;
	
	public final double derivValue;
	
	public final Property property;
	
	public final Compound compound;

	public final String name;

	public Deriv(double parameterValue, double derivValue, Property property, Compound compound, String name) {

		this.parameterValue = parameterValue;
		this.derivValue = derivValue;
		this.property = property;
		this.compound = compound;
		this.name = name;
	}
	

	
	
	
}
