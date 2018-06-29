package opt;

import opt.readers.Argument;

public class OptMain {
	

	public static void main(String... args) {
		
		Argument.readAllFilesInParameters(args);
	
		//Map<String, Object> data = Argument.DATA.getValuesFromFile();
		//for (Map.Entry<String, Object> d: data.entrySet()) {
		//	make a copy of non constant molecules
		//	System.out.println(d);
		//}
	}


}

