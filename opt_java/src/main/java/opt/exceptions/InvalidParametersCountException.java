package opt.exceptions;

@SuppressWarnings("serial")
public class InvalidParametersCountException extends RuntimeException{

		public InvalidParametersCountException(int expectedParametersCount, int actualParametersCount) {
			super(String.format("Was expected '%d' parameters, but was received '%d' parameters", expectedParametersCount, actualParametersCount));
		}
	
}
