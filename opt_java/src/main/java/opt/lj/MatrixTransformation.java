package opt.lj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import opt.ParameterTransformation;
import opt.exceptions.atomtype.InvalidMatrixException;
import opt.utils.Ask;

public class MatrixTransformation implements ParameterTransformation<List<Long>>{

	public final Map<String, Object> properties;

	
	
	public MatrixTransformation(Map<String, Object> properties) {
		this.properties = Collections.unmodifiableMap(properties);
	}



	@Override
	public List<Long> getTransformedValue() {

		Object object = this.properties.get("MATRIX");
		String sequence = (String)object;
		
		String[] split = sequence.split(" ");
		List<Long> matrix = new ArrayList<>();
		for (String matrixItem : split) {
			if(matrixItem.isEmpty()) {
				continue;
			}
			boolean isNotNumber = false == Ask.isLong(matrixItem);
			if(isNotNumber) {
				throw new InvalidMatrixException(sequence);
			}
			matrix.add(new Long(matrixItem));
		}
		return matrix;
	}

}
