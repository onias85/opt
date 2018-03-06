package opt.exceptions;

@SuppressWarnings("serial")
public class EmptyParametersException extends RuntimeException {

	public EmptyParametersException() {
		super("At least two parameters are required: the type of argument and the file relative to the argument");
	}


}
