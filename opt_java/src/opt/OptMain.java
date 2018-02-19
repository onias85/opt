package opt;

import opt.exceptions.EmptyParametersException;
import opt.exceptions.InvalidInputException;
import opt.readers.EXPECTED_PARAMETERS;

public class OptMain {

	public static void main(String... args) {
		
		int length = validateEmptyParameters(args);
		
		EXPECTED_PARAMETERS[] values = EXPECTED_PARAMETERS.values();
		for(int k = 0, m = 1; m < length ; k++, m++) {
			String paramType = args[k];
			String fileName = args[m];
			validateInput(values, paramType, fileName);
		}
		
		for (EXPECTED_PARAMETERS parameters : values) {
			System.out.println(parameters.getStatus());
			
		}
		
	}

	private static int validateEmptyParameters(String... args) {
		
		int length = args.length;
		boolean emptyParameters = length == 0;
		
		if(emptyParameters) {
			throw new EmptyParametersException();
		}
		return length;
	}

	private static void validateInput(EXPECTED_PARAMETERS[] values, String paramType, String fileName) {
	
		boolean couldNotReadIt = false == EXPECTED_PARAMETERS.readAnyFile(paramType, fileName);
		
		if(couldNotReadIt) {
			throw new InvalidInputException(values, paramType, fileName);
		}
	}
}

