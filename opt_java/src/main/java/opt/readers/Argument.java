package opt.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import opt.data.Compound;
import opt.data.Property;
import opt.exceptions.EmptyParametersException;
import opt.exceptions.IncorrectFileTypeException;
import opt.exceptions.InexistantFileException;
import opt.exceptions.InvalidFileIniFormatException;
import opt.exceptions.InvalidParametersCountException;
import opt.exceptions.InvalidSequenceParametersException;
import opt.exceptions.atomtype.InvalidMatrixException;
import opt.lj.AtomType;
import opt.utils.Ask;

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
				
				Wini wini  = new Wini(is);

				Set<String> keySet = wini.keySet();
				
				for (Object key : keySet) {
					Section section = wini.get(key);
					Map<String, String> map = Collections.unmodifiableMap(section);
					String keyValue = key.toString();
					AtomType atomType = new AtomType(keyValue, map);
					super.put(keyValue, atomType);
				}
				
				int prevSize = -1;
				Map<String, Object> map = getValuesFromFile();
				for (Object key : map.keySet()) {
					AtomType atomType = (AtomType) map.get(key);
					List<Long> matrix = atomType.getMatrix();
					int curSize = matrix.size();
					
					if (curSize != prevSize) {
						if (prevSize != -1) {	
							Long index = atomType.getIndex();
							throw new InvalidMatrixException(index);
						}
					}
					prevSize = curSize;
					
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

			
			
			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){

				int k = 0;
				
				String line;

				List<String> propertiesList = null;
				
				
				while((line = br.readLine())!= null) {
				
					boolean isTitleLine = k++ == 0;
					
					String[] split = line.split(" ");

					List<String> asList = Arrays.asList(split);
					
					asList = asList.stream().filter(s -> false == s.trim().isEmpty()).collect(Collectors.toList());
					
					split =  asList.toArray(new String[asList.size()]);
					
					if(isTitleLine) {
						
						propertiesList = this.getPropertiesList(split);
						continue;
					}

					List<Property> properties = this.extractProperties(split, propertiesList);
					String name = split[0];
					String anObject = split[1];
					Boolean constant = "Y".equals(anObject);
					Compound compound = new Compound(name, constant, properties);
					super.put(name, compound);
				}
				
				
				
				
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
			
		}

		private List<Property> extractProperties(String[] split, List<String> propertiesList) {

			List<Property> properties = new ArrayList<>();

			Double experimentalValue = 0d;
			Double simulatedValue = 0d;
			Double errorValue = 0d;
			String propertyName = "";
			int m = 0;

			for(int k = 2; k < split.length; ) {
			
				String value = split[k];
				
				boolean empty = value.trim().isEmpty();
				
				if(empty) {
					continue;
				}
				
				int index = k++ % 3;
				
				boolean isExperimental = index == 2;
				boolean isSimulated = index == 0;
				boolean isError = index == 1;
				
			
				
				
				if(isExperimental) {
					
					experimentalValue = this.getDouble(value);
					
					String string = propertiesList.get(m++);
					String[] split2 = string.split("_");
					propertyName = split2[0];
				}
				
				if(isSimulated) {
					simulatedValue = this.getDouble(value);
				}

				// error is always the last one to be evaluated, so we add the property in the list here
				if(isError) {
					errorValue = this.getDouble(value);
					Property property = new Property(experimentalValue, simulatedValue, propertyName, errorValue);
					properties.add(property);
				}
				
			}
			
			return properties;
		}

		private Double getDouble(String value) {

			boolean isNotDouble = false == Ask.isDouble(value);
			if(isNotDouble) {
				String format = String.format("The value '%s' is not double", value);
				throw new RuntimeException(format);
			}
			
			return new Double(value);
		}

		private List<String> getPropertiesList(String[] split) {
			List<String> propertiesList = new ArrayList<>(); 

			int m = 0;
			
			for (String string : split) {
				boolean empty = string.trim().isEmpty();
			
				if(empty) {
					continue;
				}
				
				
				boolean isNotPropertyName = m++ % 3 != 2;
				if(isNotPropertyName) {
					continue;
				}
				propertiesList.add(string);
			}
			return propertiesList;
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
