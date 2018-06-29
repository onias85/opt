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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}
	
	
	
	
	

}
