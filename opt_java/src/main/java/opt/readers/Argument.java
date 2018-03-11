package opt.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import opt.AtomType;
import opt.exceptions.EmptyParametersException;
import opt.exceptions.IncorrectFileTypeException;
import opt.exceptions.InexistantFileException;
import opt.exceptions.InvalidFileIniFormatException;
import opt.exceptions.InvalidParametersCountException;
import opt.exceptions.InvalidSequenceParametersException;

public 
enum Argument{
		
	CONF {
		@Override
		public void read(String fileName) {
			try {
				FileInputStream fis = new FileInputStream(fileName);
				Properties props = new Properties();
				props.load(fis);

				Set<Object> keySet = props.keySet();
				
				for (Object key : keySet) {
					Object value = props.get(key);
					super.put(key.toString(), value);
				}
				
			} catch (IOException e) {
				throw new RuntimeException("An unexpected error has occurred when reading the file " + fileName, e);
			}
		}
	},
	LJ {
		@Override
		public void read(String fileName) {
			try(InputStream is =  new FileInputStream(fileName)) {
				Wini wini  = new Wini();

				Set<String> keySet = wini.keySet();
				
				for (Object key : keySet) {
					Section section = wini.get(key);
					Map<String, String> map = Collections.unmodifiableMap(section);
					String keyValue = key.toString();
					AtomType atomType = new AtomType(keyValue, map);
					super.put(keyValue, atomType);
				}
				
			} catch (InvalidFileFormatException e) {
				throw new InvalidFileIniFormatException(fileName, e);
			}  catch (IOException e) {
				throw new RuntimeException("Unexpected error", e);
			}
			
		}
	},
	PARAM {
		@Override
		public void read(String fileName) {
			System.out.println("reading a param file called  " + fileName);
			
		}
	},
	DATA {
		@Override
		public void read(String fileName) {
			System.out.println("reading a data file called  " + fileName);
			
		}
	},
	PROP {
		@Override
		public void read(String fileName) {
			System.out.println("reading a prop file called  " + fileName);
			
		}
	},
	DERIV {
		@Override
		public void read(String fileName) {
		
			String[] array = fileName.split(",");
			boolean isArray = array.length > 1;
			
			if(isArray) {
				System.out.println("Reading an array of file of deriv: " + Arrays.asList(array));
				return;
			}
			
			File file = new File(fileName);
			
			boolean thisFileDoesNotExist = false == file.exists();
			
			if(thisFileDoesNotExist) {
				String myName = this.name();
				throw new InexistantFileException(fileName, myName);
			}
			
			boolean directory = file.isDirectory();
			
			if(directory) {
				String.format("Reading a folder called '%s' containing the files '%s'", fileName, Arrays.asList(file.list()));
				return;
			}
			
			System.out.println("Reading a single deriv file called " + fileName);
		}

		@Override
		public boolean validateFile() {
			return false;
		}
	},
	OUT {
		@Override
		public void read(String fileName) {
			
			System.out.println("writing to a out file called  " + fileName);
			
		}
		
		@Override
		public boolean validateFile() {
			return false;
		}
	}, 
	H {
		@Override
		public void read(String fileName) {
			System.out.println("Showing help content");
		}
	},

	HELP{

		@Override
		public void read(String fileName) {
			System.out.println("Showing help content"); 
		}
		
	}
	;
	private final Map<String, Object> valuesFromFile = new LinkedHashMap<>();
	
	protected void put(String key, Object value) {
		this.valuesFromFile.put(key, value);
	}
	
	protected void setValuesFromFile(Map<String, Object> valuesFromFile) {
		
		this.valuesFromFile.clear();
		this.valuesFromFile.putAll(valuesFromFile);
	}
	
	public Map<String, Object> getValuesFromFile() {
		Map<String, Object> unmodifiableMap = Collections.unmodifiableMap(valuesFromFile);
		return unmodifiableMap;
	}

	public boolean matches(String paramType) {
	
		String string = "-" + this.toString();
		boolean matches = string.equalsIgnoreCase(paramType);
		return matches;
	}
	
	private static void validateExistantFile(String fileName, String type) {
		
		File f = new File(fileName);
		
		boolean doesNotExist = false == f.exists();
		if(doesNotExist) {
			throw new InexistantFileException(fileName, type);
		}
		
		boolean directory = f.isDirectory();
		
		if(directory) {
			throw new IncorrectFileTypeException(fileName);
		}
	}

	public abstract void read(String fileName);
	
	public boolean validateFile() {
		return true;
	}
	
	public static void readAllFilesInParameters(String... args) {
		
		if(args == null) {
			throw new EmptyParametersException();
		}
		
		int cameUserArgumentsLength = args.length;
		
		if(cameUserArgumentsLength == 0) {
			throw new EmptyParametersException();
		}

		String firstParameter = args[0];
		
		boolean callingHelp = H.matches(firstParameter);
		
		if(callingHelp) {
			H.read("help.txt");
			return;
		}

		boolean callingHelpAgain = HELP.matches(firstParameter);
	
		if(callingHelpAgain) {
			HELP.read("help.txt");
			return;
		}
		
		
		Argument[] arguments = values();

		// we use "-2" because we ignore "-H" and "-Help",
		// we multiply by to "*2" because the parameters come in pairs
		// where the first one is the type of parameter and the second one the file of parameter
		int expecetedUserArgumentsLength = (arguments.length - 2) * 2;
		
		
		
		if(cameUserArgumentsLength !=  expecetedUserArgumentsLength) {
			throw new InvalidParametersCountException(expecetedUserArgumentsLength, cameUserArgumentsLength);
		}
		
		//for(int counterAboutEnum = 0, counterAboutArgsItems = 0; counterAboutEnum < cameUserArgumentsLength; counterAboutEnum++, counterAboutArgsItems += 2) {
		for(int counterAboutEnum = 0; counterAboutEnum < arguments.length - 2; counterAboutEnum++) {
			int counterAboutArgsItems = counterAboutEnum * 2;
			
			Argument argument = arguments[counterAboutEnum];
			
			String actualType = args[counterAboutArgsItems];
			String expectedType = argument.name();
			boolean doesNotMatch = false == argument.matches(actualType);
			
			if(doesNotMatch) {
				throw new InvalidSequenceParametersException(expectedType, actualType);
			}
//			System.out.println(counterAboutEnum);
//			System.out.println(counterAboutArgsItems + 1);
			System.out.println("--------------------------------");
			String filePathToRead = args[counterAboutArgsItems + 1];
			//			String msg = String.format("Lendo um '%s' no arquivo '%s'", argument.name(), filePathToRead);
//			System.out.println(msg);
			boolean validateFile = argument.validateFile();
			
			if(validateFile) {
				validateExistantFile(filePathToRead, expectedType);
				argument.read(filePathToRead);
				continue;
			}
			
			argument.read(filePathToRead);

		}
		
		
	}

}
