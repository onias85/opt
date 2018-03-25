package opt.data;

public class Property {
	

	public final Double experimentalValue;
	
	public final Double simulatedValue;

	public final String propertyName;
	
	public final Double errorValue;

	public Property(Double experimentalValue, Double simulatedValue, String propertyName, Double errorValue) {

		this.experimentalValue = experimentalValue;
		this.simulatedValue = simulatedValue;
		this.propertyName = propertyName;
		this.errorValue = errorValue;
	}

	@Override
	public String toString() {
		return "Property [experimentalValue=" + experimentalValue + ", simulatedValue=" + simulatedValue
				+ ", propertyName=" + propertyName + ", errorValue=" + errorValue + "]";
	}
	
	
	

}
