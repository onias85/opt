package opt.exceptions;

import java.util.Arrays;

import opt.readers.EXPECTED_PARAMETERS;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException {


	public InvalidInputException(EXPECTED_PARAMETERS[] values, String paramType, String fileName) {
		super(getErrorMessage(values, paramType, fileName));
	}
	
	private static String getErrorMessage(EXPECTED_PARAMETERS[] values, String paramType, String fileName) {
		String errorMessage = String.format("The parameter with the type '%s' and with the value '%s' could not be identified. Are expected the follow parameter names: %s ",paramType, fileName, Arrays.asList(values).toString());
		return errorMessage;
	}

}
