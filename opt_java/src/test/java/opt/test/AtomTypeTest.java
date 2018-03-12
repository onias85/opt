package opt.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.junit.Assert;
import org.junit.Test;

import opt.AtomType;
import opt.exceptions.InvalidMatrixException;
import opt.exceptions.PropertyIsMissingException;
import opt.readers.Argument;

public class AtomTypeTest {
	
	private Map<String,  String> map = new HashMap<>();
	
	@Test(expected = PropertyIsMissingException.class)
	public void idxIsMissingTest() {
		AtomType emptyAtomType = new AtomType("", map);
	}
		
	@Test(expected = PropertyIsMissingException.class)
	public void nameIsMissingTest() {
		AtomType emptyAtomType = new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void constIsMissingTest() {
		map.put("NAME", "OA");
		AtomType emptyAtomType = new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c06MissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		AtomType emptyAtomType = new AtomType("1", map);
	}
	
	@Test(expected = PropertyIsMissingException.class)
	public void c121MissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		AtomType emptyAtomType = new AtomType("1", map); 
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c122MissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		map.put("C12_1", "0.00");
		AtomType emptyAtomType = new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c123MissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		map.put("C12_1", "0.00");
		map.put("C12_2", "0.00");
		AtomType emptyAtomType = new AtomType("1", map);
	}
	
	@Test(expected = PropertyIsMissingException.class)
	public void c06NBMissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		map.put("C12_1", "0.00");
		map.put("C12_2", "0.00");
		map.put("C12_3", "0.00");
		AtomType emptyAtomType = new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c12NBMissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		map.put("C12_1", "0.00");
		map.put("C12_2", "0.00");
		map.put("C12_3", "0.00");
		map.put("C06_NB", "0.00");
		AtomType emptyAtomType = new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void matrixMissingTest() {
		map.put("NAME", "OA");
		map.put("CONST", "True");
		map.put("C06", "0.00");
		map.put("C12_1", "0.00");
		map.put("C12_2", "0.00");
		map.put("C12_3", "0.00");
		map.put("C06_NB", "0.00");
		map.put("C12_NB", "0.00");
		AtomType emptyAtomType = new AtomType("1", map);
	}
	
	@Test(expected = InvalidMatrixException.class)
	public void noNumericValinMatrixTest(){
		Argument.LJ.read("lj_matrix_nonnumeric.txt");
	}
	
//	public static void main(String[] args) throws Exception {
//		Wini wini = new Wini(new File("marina.ini"));
//		Section section = wini.get("71");
//		AtomType at = new AtomType("71", section);
//		System.out.println(at);
//	}

}
