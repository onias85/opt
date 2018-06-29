package opt.utils;

public class DoubleDecorator {

	public final Double value;
	
	public DoubleDecorator(String variableName, String variableValue) {
		try {
			this.value = new Double(variableValue);
		} catch (NumberFormatException e) {
			throw new IncorrectValueException(variableName, variableValue, e);
		}
	}
	
	@SuppressWarnings("serial")
	public static class IncorrectValueException extends RuntimeException{
		
		private IncorrectValueException(String variableName, String variableValue, NumberFormatException e) {
			super(getErrorMessage(variableName, variableValue), e);
		}

		private static String getErrorMessage(String variableName, String variableValue) {
			String format = String.format("The variavble '%s' with the value '%s' could not be converted to a valid double", variableName, variableValue);
			return format;
		}
	}
}
