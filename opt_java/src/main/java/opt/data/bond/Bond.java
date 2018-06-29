package opt.data.bond;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Bond {
	
	public final String cod; 
	
	public final int index1;
	
	public final int index2;
	
	public final int type;
	
	public Bond(String cod, int idx1, int idx2, int typ) {
		this.cod = cod;
		this.index1 = idx1;
		this.index2 = idx2;
		this.type = typ;
	}
	
	public Bond setType(int type) {
		return new Bond(cod, index1, index2, type);
	}

	@Override
	public String toString() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonBuilder setPrettyPrinting = gsonBuilder.setPrettyPrinting();
		Gson create = setPrettyPrinting.create();
		String json = create.toJson(this);
		return json;
	}
	
	
}
