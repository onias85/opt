package opt.exceptions.atomtype;

@SuppressWarnings("serial")
public class InvalidMatrixException extends RuntimeException{

	public InvalidMatrixException(String matrix) {
		super(getErrorMessageWrongType(matrix));
	}
	
	public InvalidMatrixException(Long idx) {
		super(getErrorMessageWrongSize(idx));
	}

	private static String getErrorMessageWrongType(String matrix) {

		return String.format("The matrix '%s' is invalid because some of this values in this matrix are not integer number", matrix);
	}
	
	private static String getErrorMessageWrongSize(Long idx) {

		return String.format("Size mismatch for matrix of atom type with IAC = %s", idx);
	}
	
}
