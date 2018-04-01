package opt.exceptions;

import opt.Format;

@SuppressWarnings("serial")
public class IncompatibleFormatException extends RuntimeException{

	
	public IncompatibleFormatException(String propertyName, Format format) {
		super(getErrorMessage(propertyName, format));
	}

	private static String getErrorMessage(String propertyName, Format f) {
		String format = String.format("The property named '%s' is not compatible with the format '%s' ", propertyName, f.name());
		return format;
	}
}
