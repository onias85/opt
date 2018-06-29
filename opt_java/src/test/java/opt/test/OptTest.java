package opt.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import opt.exceptions.EmptyParametersException;
import opt.exceptions.InvalidFileIniFormatException;
import opt.exceptions.InvalidParametersCountException;
import opt.readers.Argument;

public class OptTest {

	@Test(expected = EmptyParametersException.class)
	public void testNullParameters() {
		String[] array = null;
		Argument.readAllFilesInParameters(array);
	}

	@Test(expected = EmptyParametersException.class)
	public void testEmtyParameters() {
		String[] array = new String[] {};
		Argument.readAllFilesInParameters(array);
	}
	
	@Test
	public void testAllValidPosibleMatchesToHelpMenu() {
		Argument.readAllFilesInParameters("-H");
		Argument.readAllFilesInParameters("-h");
		Argument.readAllFilesInParameters("-Help");
		Argument.readAllFilesInParameters("-help");
		Argument.readAllFilesInParameters("-HELP");
		Argument.readAllFilesInParameters("-hElp");
	}
	

	@Test(expected = InvalidParametersCountException.class)
	public void testInvalidParametersCountException() {
		Argument.readAllFilesInParameters("-data");
		
	}

	@Test(expected = InvalidParametersCountException.class)
	public void testInvalidTooMuchParametersCountException() {
		Argument.readAllFilesInParameters("-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt","-conf", "conf.txt");
		
	}
	
	@Test
	public void readingCorrectly(){
		Argument.readAllFilesInParameters(
				"-conf", "conf.txt",
				"-lj", "lj.txt",
				"-param", "param.txt",
				"-data", "data.txt",
				"-prop", "prop.txt",
				"-deriv", "deriv",
				"-out", "out.txt"
				);
		
		
		Argument.CONF.read("conf.txt");
		Map<String, Object> valuesFromFile = Argument.CONF.getValuesFromFile();
		
		int configurationsSize = valuesFromFile.size();
		Assert.assertTrue(configurationsSize == 6);
	}

	@Test
	public void readingDerivCorrectly(){
		Argument.readAllFilesInParameters(
				"-conf", "conf.txt",
				"-lj", "lj.txt",
				"-param", "param.txt",
				"-data", "data.txt",
				"-prop", "prop.txt",
				"-deriv", "deriv",
				"-out", "out.txt"
				);
		
		
		Argument.CONF.read("conf.txt");
		Map<String, Object> valuesFromFile = Argument.DERIV.getValuesFromFile();
		
		int configurationsSize = valuesFromFile.size();
		Assert.assertTrue(configurationsSize == 14);
	}

	@Test(expected = InvalidFileIniFormatException.class)
	public void incorrectIniFormatException() {
		
		Argument.LJ.read("wrongFormatMatrix.ini");
	}
	
	@Test
	public void testCorrectReadingToData() {
		Argument.DATA.read("data.txt");
		Map<String, Object> valuesFromFile = Argument.DATA.getValuesFromFile();
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(valuesFromFile));
		
		int size = valuesFromFile.size();
		Assert.assertTrue(7 == size);
	}
	
	
	@Test
	public void testPropReading() {
		Argument.CONF.read("prop.txt");
		Map<String, Object> valuesFromFile = Argument.CONF.getValuesFromFile();
		boolean isCorrect = valuesFromFile.size() == 2;
		assertTrue(isCorrect);
	}
}
