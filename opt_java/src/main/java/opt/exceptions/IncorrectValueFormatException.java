package opt.exceptions;

@SuppressWarnings("serial")
public class IncorrectValueFormatException extends RuntimeException {

	public IncorrectValueFormatException(String value, Class<?> clazz) {
		super(getErrorMessage(value, clazz));
	}

	private static String getErrorMessage(String value, Class<?> clazz) {

		String format = String.format("The value '%s' is not compatible with '%s'", value, clazz.getSimpleName());
		return format;
	}
	
}
