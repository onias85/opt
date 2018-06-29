package onemax_problem;

import java.lang.reflect.InvocationTargetException;

public class Main {

	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		Tester tester = new Tester();
		// severity: 0.1, 0.2, 0.5, 0.9 and period: 1200, 3000, 6000, 9000, 12000
		int period = Integer.valueOf(args[0]);
		float severity = Float.valueOf(args[1]);
		tester.MG(severity, period);
		
	}
}
