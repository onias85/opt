package opt_java;

import java.util.Arrays;

import opt_java.readers.EXPECTED_PARAMETERS;

public class OptMain {

	public static void main(String[] args) {
		
		int length = args.length;
		boolean emptyParameters = length == 0;
		
		if(emptyParameters) {
			throw new RuntimeException("Empty parameters");
		}
		
		boolean isNotEvenNumbers = length % 2 != 0;
		
		if(isNotEvenNumbers) {
			throw new RuntimeException("The number of parameters has to be even, because the first one of each pair means the type and the second one of the pair means the file with the content");
			
		}
		
		for(int k = 0, m = 1; m < length ; k++, m++) {
			String paramType = args[k];
			String fileName = args[m];
			boolean couldNotReadIt = false == EXPECTED_PARAMETERS.readAnyFile(paramType, fileName);
			
			if(couldNotReadIt) {
				String errorMessage = String.format("The parameter with the type '%s' e com valor '%s' could not be identified. Are expected the follow parameter names: %s ",paramType, fileName, Arrays.asList(EXPECTED_PARAMETERS.values()).toString());
				throw new RuntimeException(errorMessage);
			}
		}
		
		System.out.println();
		
	}
}

