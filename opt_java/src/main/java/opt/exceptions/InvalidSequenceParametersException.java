package opt.exceptions;

@SuppressWarnings("serial")
public class InvalidSequenceParametersException extends RuntimeException {


	public InvalidSequenceParametersException(String expectedType, String actualType) {
		super(String.format("was expected now the word '%s', but received the word '%s'", expectedType, actualType));
	}
	
}
