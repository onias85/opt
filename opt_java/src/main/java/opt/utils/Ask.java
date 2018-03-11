package opt.utils;

public class Ask {

	public static boolean isDouble(String str) {
		try {
			new Double(str);	
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}

	public static boolean isLong(String str) {
		try {
			new Long(str);	
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}

	public static boolean isBoolean(String str) {
		if("true".equalsIgnoreCase(str)) {
			return true;
		}

		if("false".equalsIgnoreCase(str)) {
			return true;
		}
		
		return false;
	}
	
}
