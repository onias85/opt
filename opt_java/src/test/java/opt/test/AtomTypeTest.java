package opt.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import opt.AtomType;
import opt.exceptions.PropertyIsMissingException;

public class AtomTypeTest {
	
	private Map<String,  String> emptyMap = new HashMap<>();
	private AtomType emptyAtomType = new AtomType("11", this.emptyMap);
	
	
	@Test(expected = PropertyIsMissingException.class)
	public void nameIsMissingTest() {
		this.emptyAtomType.getName();
	}

	
	
}
