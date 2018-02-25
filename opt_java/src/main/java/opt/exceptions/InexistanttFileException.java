package opt.exceptions;

@SuppressWarnings("serial")
public class InexistanttFileException extends RuntimeException {


	public InexistanttFileException(String fileName, String type) {
		super("The file '" + fileName + "' does not exist, he is required to read a " + type);
	}
}
