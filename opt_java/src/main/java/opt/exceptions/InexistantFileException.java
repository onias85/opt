package opt.exceptions;

@SuppressWarnings("serial")
public class InexistantFileException extends RuntimeException {


	public InexistantFileException(String fileName, String type) {
		super("The file '" + fileName + "' does not exist, he is required to read a " + type);
	}
}
