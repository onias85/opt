package opt.data.angle;

import com.google.gson.GsonBuilder;

public class Angle {
	public final String compoundName;
	public final int index1;
	public final int index2;
	public final int index3;
	public final int angleIndex;

	public Angle(String compoundName, int index1, int index2, int index3, int angleIndex) {
		super();
		this.compoundName = compoundName;
		this.index1 = index1;
		this.index2 = index2;
		this.index3 = index3;
		this.angleIndex = angleIndex;
	}

	@Override
	public String toString() {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
		return json;
	}

}
