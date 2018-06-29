package opt.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opt.data.angle.Angle;
import opt.data.atom.Atom;
import opt.data.bond.Bond;

public class Compound {

	public final String name;
	
	public final Boolean constant;
	
	public final Set<Property> properties;
	
	public Map<Integer, Atom> atoms = new HashMap<Integer, Atom>();
	
	public List<Bond> bonds = new ArrayList<>();

	public List<Angle> angles = new ArrayList<>();

	public Compound(String name, Boolean constant, Set<Property> properties) {

		this.properties = Collections.unmodifiableSet(properties);
		this.constant = constant;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Compound [name=" + name + ", constant=" + constant + ", properties=" + properties + ", atoms=" + atoms + "]";
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAtoms(Map<Integer, Atom> atoms) {
		this.atoms = new HashMap<>( atoms);
	}
	
	public boolean hasNoAtom() {
		return atoms.isEmpty();
	}
	
	public boolean hasOnlyOneAtom() {
		return (atoms.size() == 1);
	}
	
	public Map<Integer, Atom> getAtoms() {
		return atoms;
	}
	
	public void setBonds(List<Bond> bonds) {
		this.bonds = bonds;
	}
	
	public boolean hasNoBond() {
		return bonds.isEmpty();
	}
	
	
	public void setFirstNeighbour(Map<String, Object> bondTypes) {
		
		for (Bond bond : this.bonds) {

			Atom atom1 = this.atoms.get(bond.index1);	
			Atom atom2 = this.atoms.get(bond.index2);
			double dist = (Double)bondTypes.get( "" + bond.type);
			atom1.addFirstNeighbour(bond.index2, dist);
			atom2.addFirstNeighbour(bond.index1, dist);
		}
	}

	public void setAngles(List<Angle> anglesWithThisCompoundName) {
		this.angles = anglesWithThisCompoundName;
	}
	

	public void setSecondNeighbour(Map<String, Object> anglesTypes) {
		
		for (Angle angle : this.angles) {
			// we assume that index 1 is the middle atom
			
			Atom atom1 = this.atoms.get(angle.index1);	
			Atom atom3 = this.atoms.get(angle.index3);
			
			double distFirstToSecond = atom1.getDistance(angle.index2);
			double distSecondToThird = atom3.getDistance(angle.index2);
			
			Object object = anglesTypes.get("" + angle.angleIndex);
			
			double ang = (Double)object;
			
			double equationFirstPiece = Math.pow(distFirstToSecond, 2) + 
					Math.pow(distSecondToThird, 2) - 
					(2 * distFirstToSecond * distSecondToThird) 
					* Math.cos(ang) ;
			
 			double distFirstToThird = Math.sqrt(equationFirstPiece);
			atom1.addSecondNeighbour(angle.index3, distFirstToThird);
			atom3.addSecondNeighbour(angle.index1, distFirstToThird);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Math.sqrt(9));
	}
}
