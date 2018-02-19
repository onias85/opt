package opt.exceptions;

@SuppressWarnings("serial")
public class EmptyValueException extends RuntimeException{

	public EmptyValueException(String parameterName) {
		super("The parameter '" + parameterName + "' is null or empty");
	}

}
