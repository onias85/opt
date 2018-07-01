package library_example;
import java.util.Arrays;

import Jama.Matrix;

public class matrix_jama {


	public static void main(String[] args) {
		//double[][] array = {{1.,2.,3},{1.,2.,3.},{1.,2.,3.}}; 
		//Matrix a = new Matrix(array);   
		//Matrix b = new Matrix(new double[]{1., 1., 1.}, 1);     
		//Matrix c = b.times(a);  
		//System.out.println(Arrays.deepToString(a.getArray()));
		
		double[][] array = {{1.,1.,1},{0.,2.,5.},{2.,5.,-1.}};
		Matrix A = new Matrix(array);
		//Matrix b = Matrix.random(3,1);
		double[][] array2 = {{6.},{-4.},{27.}}; 
		Matrix b = new Matrix(array2);
		Matrix x = A.solve(b);
		System.out.println(Arrays.deepToString(A.getArray()));
		System.out.println(Arrays.deepToString(b.getArray()));
		System.out.println(Arrays.deepToString(x.getArray()));
	}
}
