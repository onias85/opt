package opt.test;

import org.junit.Test;

import opt.exceptions.EmptyParametersException;
import opt.exceptions.InvalidParametersCountException;
import opt.readers.Parameter;

public class OptTest {

	@Test(expected = EmptyParametersException.class)
	public void testNullParameters() {
		String[] array = null;
		Parameter.readAllFilesInParameters(array);
	}

	@Test(expected = EmptyParametersException.class)
	public void testEmtyParameters() {
		String[] array = new String[] {};
		Parameter.readAllFilesInParameters(array);
	}
	
	@Test
	public void testAllValidPosibleMatchesToHelpMenu() {
		Parameter.readAllFilesInParameters("-H");
		Parameter.readAllFilesInParameters("-h");
		Parameter.readAllFilesInParameters("-Help");
		Parameter.readAllFilesInParameters("-help");
		Parameter.readAllFilesInParameters("-HELP");
		Parameter.readAllFilesInParameters("-hElp");
	}
	

	@Test(expected = InvalidParametersCountException.class)
	public void testInvalidParametersCountException() {
		Parameter.readAllFilesInParameters("-data");
		
	}

	@Test(expected = InvalidParametersCountException.class)
	public void testInvalidTooMuchParametersCountException() {
		Parameter.readAllFilesInParameters("-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt");
		
	}
	
	
}
