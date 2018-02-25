package opt.exceptions;

@SuppressWarnings("serial")
public class EmptyParametersException extends RuntimeException {

	public EmptyParametersException() {
		super("Are required at least two parameters, being the first one the type of parameter and the second one the file of the parameter");
	}


}
