package opt.exceptions;

import org.ini4j.InvalidFileFormatException;

@SuppressWarnings("serial")
public class InvalidFileIniFormatException extends RuntimeException{

	public InvalidFileIniFormatException(String fileName, InvalidFileFormatException e) {
		super(getErrorMessage(fileName), e);// passamos o "e" como segundo parametro, para manter a rastreabilidade das exceptions, ou seja, qual exceção gerou qual exceção
	}

	private static String getErrorMessage(String fileName) {
		return String.format("The file '%s' is invalid to ini format file", fileName);
	}
	
}
