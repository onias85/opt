package opt.exceptions;

@SuppressWarnings("serial")
public class InexistanttFileException extends RuntimeException {


	public InexistanttFileException(String fileName) {
		super("The file '" + fileName + "' does not exist");
	}
}
