package opt.exceptions;

@SuppressWarnings("serial")
public class IncorrectFileTypeException extends RuntimeException {


	public IncorrectFileTypeException(String fileName) {
		super("The file '" + fileName + "' is a folder");
	}
}
