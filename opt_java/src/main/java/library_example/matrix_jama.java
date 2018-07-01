package library_example;
import java.util.Arrays;

import Jama.Matrix;

public class matrix_jama {


	public static void main(String[] args) {
		double[][] array = {{1.,2.,3},{1.,2.,3.},{1.,2.,3.}}; 
		Matrix a = new Matrix(array);   
		Matrix b = new Matrix(new double[]{1., 1., 1.}, 1);     
		Matrix c = b.times(a);  
		System.out.println(Arrays.deepToString(c.getArray()));

	}
}
