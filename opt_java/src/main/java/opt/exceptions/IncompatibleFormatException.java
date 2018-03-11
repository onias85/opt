package opt.exceptions;

@SuppressWarnings("serial")
public class IncompatibleFormatException extends RuntimeException{

	
	public IncompatibleFormatException(String propertyName, Class<?> clazz) {
		super(getErrorMessage(propertyName, clazz));
	}

	private static String getErrorMessage(String propertyName, Class<?> clazz) {
		String format = String.format("The property named '%s' is not compatible with the format '%s' ", propertyName, clazz.getSimpleName());
		return format;
	}
}
