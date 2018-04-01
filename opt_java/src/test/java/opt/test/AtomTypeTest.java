package opt.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import opt.exceptions.atomtype.InvalidMatrixException;
import opt.exceptions.atomtype.PropertyIsMissingException;
import opt.lj.AtomType;
import opt.readers.Argument;

public class AtomTypeTest {
	
	private static final String MATRIX = "MATRIX";
	private static final String C12_NB = "C12_NB";
	private static final String C06_NB = "C06_NB";
	private static final String C12_3 = "C12_3";
	private static final String C12_2 = "C12_2";
	private static final String C12_1 = "C12_1";
	private static final String C06 = "C06";
	private static final String CONST = "CONST";
	private static final String NAME = "NAME";
	private Map<String,  Object> map = new HashMap<>();
	
	@Test(expected = PropertyIsMissingException.class)
	public void idxIsMissingTest() {
		
		new AtomType("", map);
	}
		
	@Test(expected = PropertyIsMissingException.class)
	public void nameIsMissingTest() {

		this.map.remove(NAME);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void constIsMissingTest() {

		this.map.remove(CONST);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c06MissingTest() {

		this.map.remove(C06);
		new AtomType("1", map);
	}
	
	@Test(expected = PropertyIsMissingException.class)
	public void c121MissingTest() {

		this.map.remove(C12_1);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c122MissingTest() {

		this.map.remove(C12_2);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c123MissingTest() {

		this.map.remove(C12_3);
		new AtomType("1", map);
	}
	
	@Test(expected = PropertyIsMissingException.class)
	public void c06NBMissingTest() {

		this.map.remove(C06_NB);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c12NBMissingTest() {

		this.map.remove(C12_NB);
		new AtomType("1", map);
	}

	@Test(expected = PropertyIsMissingException.class)
	public void matrixMissingTest() {
		this.map.remove(MATRIX);
		new AtomType("1", map);
	}
	
	@Test(expected = InvalidMatrixException.class)
	public void noNumericValinMatrixTest(){
		Argument.LJ.read("lj_matrix_nonnumeric.txt");
	}
	
	@Test(expected = InvalidMatrixException.class)
	public void matrixMismatchSizeTest() {
		Argument.LJ.read("lj_matrix_mismatch_size.txt");
	}
	
	
	@Before
	public void setup() {
		this.map.put(NAME, "OA");
		this.map.put(CONST, "True");
		this.map.put(C06, "0.00");
		this.map.put(C12_1, "0.00");
		this.map.put(C12_2, "0.00");
		this.map.put(C12_3, "0.00");
		this.map.put(C06_NB, "0.00");
		this.map.put(C12_NB, "0.00");
		this.map.put(MATRIX, "1 1 1");
	}

}
