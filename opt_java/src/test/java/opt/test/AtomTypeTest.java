package opt.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.junit.Test;

import opt.AtomType;
import opt.exceptions.PropertyIsMissingException;

public class AtomTypeTest {
	
	private Map<String,  String> emptyMap = new HashMap<>();
	private AtomType emptyAtomType = new AtomType("", this.emptyMap);
	
	
	@Test(expected = PropertyIsMissingException.class)
	public void nameIsMissingTest() {
		this.emptyAtomType.getName();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void indexIsMissingTest() {
		this.emptyAtomType.getIndex();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void constIsMissingTest() {
		this.emptyAtomType.isConst();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c06MissingTest() {
		this.emptyAtomType.getC06();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c121MissingTest() {
		this.emptyAtomType.getC121(); 
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c122MissingTest() {
		this.emptyAtomType.getC122();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c123MissingTest() {
		this.emptyAtomType.getC123();
	}
	
	
	@Test(expected = PropertyIsMissingException.class)
	public void c06NBMissingTest() {
		this.emptyAtomType.getC06NB();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void c12NBMissingTest() {
		this.emptyAtomType.getC12NB();
	}

	@Test(expected = PropertyIsMissingException.class)
	public void matrixMissingTest() {
		this.emptyAtomType.getMatrix();
	}
	
	public static void main(String[] args) throws Exception {
		Wini wini = new Wini(new File("marina.ini"));
		Section section = wini.get("71");
		AtomType at = new AtomType("71", section);
		System.out.println(at);
	}

}
