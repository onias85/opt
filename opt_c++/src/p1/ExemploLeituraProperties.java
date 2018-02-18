package p1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ExemploLeituraProperties {


	public static void main(String[] args) {
		Properties prop = new Properties();
		try {
//          load a properties file from class path, inside static method
//		    InputStream as = ExemploLeituraProperties.class.getClassLoader().getResourceAsStream("config.properties");
		    InputStream is = new java.io.FileInputStream("config.properties");
			prop.load(is);

		    //get the property value and print it out
		    System.out.println(prop.getProperty("database"));
		    System.out.println(prop.getProperty("dbuser"));
		    System.out.println(prop.getProperty("dbpassword"));
		    
		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
	
}
