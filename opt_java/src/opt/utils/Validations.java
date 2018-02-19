package opt.utils;

import opt.exceptions.EmptyValueException;

public class Validations {

	public static void notEmptyParameter(String parameterName, Object parameterValue) {
		
		if(parameterValue == null) {
			throw new EmptyValueException(parameterName);
		}
		
		if(parameterValue.toString() == null) {
			throw new EmptyValueException(parameterName);
		}
		
		if(parameterValue.toString().isEmpty()) {
			throw new EmptyValueException(parameterName);
		}
	}

}
