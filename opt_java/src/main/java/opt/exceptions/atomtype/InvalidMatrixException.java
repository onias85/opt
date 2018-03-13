package opt.exceptions.atomtype;

@SuppressWarnings("serial")
public class InvalidMatrixException extends RuntimeException{

	public InvalidMatrixException(String matrix) {
		super(getErrorMessage(matrix));
	}

	private static String getErrorMessage(String matrix) {

		return String.format("The matrix '%s' is invalid because some of this values in this matrix are not integer number", matrix);
	}
	
}
