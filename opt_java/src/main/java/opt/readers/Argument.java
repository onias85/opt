package opt.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import opt.data.angle.Angle;
import opt.data.atom.Atom;
import opt.data.bond.Bond;
import opt.data.deriv.Charge;
import opt.data.deriv.Deriv;
import opt.exceptions.EmptyParametersException;
import opt.exceptions.IncorrectFileTypeException;
import opt.exceptions.InexistantFileException;
import opt.exceptions.InvalidFileIniFormatException;
import opt.exceptions.InvalidParametersCountException;
import opt.exceptions.InvalidSequenceParametersException;
import opt.exceptions.atomtype.InvalidMatrixException;
import opt.lj.AtomType;
import opt.utils.Ask;
import opt.utils.IntegerDecorator;

public 
enum Argument{
		
	CONF {
		@Override
		public void read(String fileName) {
			super.putAll(fileName);

		}
	},
	BONDTYPE {
		@Override
		public void read(String fileName) {

			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){
				
				String line;
				
				while((line = br.readLine())!= null) {
					String[] split = line.split(" ");
					List<String> asList = Arrays.asList(split);
					asList = asList.stream().filter(s -> false == s.trim().isEmpty()).collect(Collectors.toList());
					split =  asList.toArray(new String[asList.size()]);

					String idx = split[0];
					Double r = Double.parseDouble(split[3]);
					super.put(idx, r);
				}
				
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
	},
	ANGLETYPE {
		@Override
		public void read(String fileName) {

			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){
				
				String line;
				
				while((line = br.readLine())!= null) {
					String[] split = line.split(" ");
					List<String> asList = Arrays.asList(split);
					asList = asList.stream().filter(s -> false == s.trim().isEmpty()).collect(Collectors.toList());
					split =  asList.toArray(new String[asList.size()]);
					
					// why if reading an empty line?
					if (split.length == 0) {
						continue;
					}

					String idx = split[0];
					Double r = Double.parseDouble(split[3]);
					super.put(idx, r);
				}
				
			}catch(IOException e) {
				throw new RuntimeException(e);
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
//					AtomType atomType = new AtomType(keyValue, map);
					AtomType atomType = new AtomType(map, keyValue);
					
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

					Set<Property> properties = this.extractProperties(split, propertiesList);
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

		private Set<Property> extractProperties(String[] split, List<String> propertiesList) {

			Set<Property> properties = new HashSet<>();

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

		public List<String> getPropertiesList(String[] split) {
			
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
	ATOM {
		@Override
		public void read(String fileName) {
			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){
				
				Map<String, Object> data = Argument.DATA.getValuesFromFile();
				// does not work with int
				Map<Integer, Atom> atoms = new HashMap<Integer, Atom>();
				// should it be null instead of ""?
				String prevCod = "";
				
				String line = br.readLine();
				while(line != null) {
					String[] split = line.split(" ");
					List<String> asList = Arrays.asList(split);
					asList = asList.stream().filter(s -> false == s.trim().isEmpty()).collect(Collectors.toList());
					split =  asList.toArray(new String[asList.size()]);
					
					String cod = split[0];
					// error prone
					int atmIdx = Integer.parseInt(split[1]);
					String atmNam = split[2];
					int atmTyp = Integer.parseInt(split[3]);
					Double atmCHG = Double.parseDouble(split[4]);

					if (!prevCod.isEmpty() && !prevCod.equals(cod)) {
						super.put(prevCod, atoms);
						Compound cmp = (Compound) data.get(prevCod);
						cmp.setAtoms(atoms);
						atoms = new HashMap<Integer, Atom>();
					}

					Atom atm = new Atom(cod, atmIdx, atmNam, atmTyp, atmCHG);
					// check if atmIdx is already in atoms
					atoms.put(atmIdx, atm);
					prevCod = cod;
					
					line = br.readLine();
					if (line  == null) {
						super.put(cod, atoms);
						Compound cmp = (Compound) data.get(prevCod);
						cmp.setAtoms(atoms);
					}
				}
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
			
			// assign atoms to compounds
			check();
		}
		
		private void check() {
			Map<String, Object> data = Argument.DATA.getValuesFromFile();
			
			// check if all compounds have a non empty map of atoms
			for (Map.Entry<String, Object> d: data.entrySet()) {
				Compound cmp = (Compound) d.getValue();
				if (cmp.hasNoAtom()) {
					throw new RuntimeException("No atoms");
				}
			}
		}
	},

	BOND {
		@Override
		public void read(String fileName) {
			System.out.println("reading a file called  " + fileName);
			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){
				
				Map<String, Object> data = Argument.DATA.getValuesFromFile();
				List<Bond> bonds = new ArrayList<>();
				String prevCod = "";
				int k = 0;
				
				String line = br.readLine();
				while(line != null) {

					String[] split = super.splitThisLine(line);
					
					String cod = split[0];
					int idx1 = Integer.parseInt(split[1]);
					int idx2 = Integer.parseInt(split[2]);
					int typ = Integer.parseInt(split[3]);
					
					if (!prevCod.isEmpty() && !prevCod.equals(cod)) {
						super.put(prevCod, bonds);
						// already add bonds to compounds
						Compound cmp = (Compound) data.get(prevCod);
						cmp.setBonds(bonds);
						bonds = new ArrayList<>();
						k = 0;
					}
					
					Bond bnd = new Bond(cod, idx1, idx2, typ);
					bonds.add(bnd);
					prevCod = cod;
					k = k + 1;
					
					line = br.readLine();
					if (line  == null) {
						super.put(cod, bonds);
						Compound cmp = (Compound) data.get(prevCod);
						cmp.setBonds(bonds);
					}
				}
				
				// here or outside of try/catch?
				this.check();
//				this.computeDist();
				
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private void check() {
			Map<String, Object> data = Argument.DATA.getValuesFromFile();
			for (Map.Entry<String, Object> d: data.entrySet()) {
				Compound cmp = (Compound) d.getValue();
				if (cmp.hasNoBond()) {
					if (!cmp.hasOnlyOneAtom()) {
						throw new RuntimeException("No bonds");
					}
				}
			}
		}
		
		
	},
	
	ANGLE {
		@Override
		public void read(String fileName) {

			System.out.println("reading a file called  " + fileName);
			
			try(FileReader fr = new FileReader(new File(fileName)); BufferedReader br = new BufferedReader(fr)){
				String line;

				List<Angle> angles = new ArrayList<>();
				
				while((line = br.readLine()) != null) {
					
					String[] split = super.splitThisLine(line);
						
					String compoundName = split[0];
					
					String val1 = split[1];
					String val2 = split[2];
					String val3 = split[3];
					String val4 = split[4];
					
					int index1 = new IntegerDecorator("split[1]", val1).value;
					int index2 = new IntegerDecorator("split[2]", val2).value;
					int index3 = new IntegerDecorator("split[3]", val3).value;
					int index4 = new IntegerDecorator("split[4]", val4).value;
					Angle angle = new Angle(compoundName, index1, index2, index3, index4);
					angles.add(angle);
					
				}
				
				Map<String, Object> compounds = Argument.DATA.getValuesFromFile();
				Set<String> allCompoundNames = angles.stream().map(angle -> angle.compoundName).collect(Collectors.toSet());
				
				for (String compoundName : allCompoundNames) {
				
					List<Angle> anglesWithThisCompoundName = angles.stream().filter(angle -> angle.compoundName.equals(compoundName)).collect(Collectors.toList());
					super.put(compoundName, anglesWithThisCompoundName);
					Object object = compounds.get(compoundName);
					Compound compound = (Compound)object;
					compound.setAngles(anglesWithThisCompoundName);
				}
				
			}catch(IOException e) {
				throw new RuntimeException(e);
			}	
		// do a check
			this.computeDist();
		}
		// should this enum have this function?
		private void computeDist() {
			
			Map<String, Object> angleTypes = Argument.ANGLETYPE.getValuesFromFile();
			Map<String, Object> bondTypes = Argument.BONDTYPE.getValuesFromFile();
			Map<String, Object> data = Argument.DATA.getValuesFromFile();

			for (Map.Entry<String, Object> d: data.entrySet()) {
				Compound compound = (Compound) d.getValue();
				compound.setFirstNeighbour(bondTypes);
				compound.setSecondNeighbour(angleTypes);
			}
		}
	},
	
	PROP {
		@Override
		public void read(String fileName) {

			super.putAll(fileName);
			
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
			
			boolean isFolder = file.isDirectory();
			
			if(isFolder) {
				
				String msg = String.format("Reading a folder called '%s' containing the files '%s'", fileName, Arrays.asList(file.list()));
			
				System.out.println(msg);
				
				Map<String, Object> compounds = Argument.DATA.getValuesFromFile();
				
				Set<String> keySet = compounds.keySet();
				
				List<String> existingDerivs = Arrays.asList(file.listFiles()).stream().map(f -> f.getName()).collect(Collectors.toList());

				List<String> errors = new ArrayList<>();
				
				for (String compoundName : keySet) {
					
					Object obj = compounds.get(compoundName);
					
					Compound compound = (Compound)obj;
					
					Set<Property> properties = compound.properties;

					List<Deriv> derivateds = new ArrayList<>();
					
					for (Property property : properties) {
						
						StringBuilder derivName = new StringBuilder(compoundName).append("_").append(property.propertyName).append(".dat");
						
						boolean derivNotFound = false == existingDerivs.stream().map(x -> x.toUpperCase()).collect(Collectors.toList()).contains(derivName.toString().toUpperCase());	
						
						if(derivNotFound) {
							String absolutePath = file.getAbsolutePath();
							String format = String.format("The derivated file '%s' was not found in the folder '%s'", derivName, absolutePath);
							errors.add(format);
							continue;
						}
						
						boolean hasErrors = false == errors.isEmpty();
						
						if(hasErrors) {
							throw new RuntimeException("faltando os arquivos " + errors);
						}
						
						String absolutePath = new File(fileName).getAbsolutePath();
						String completePath = absolutePath + "/" + derivName;
						
						try(FileReader fr = new FileReader(completePath); BufferedReader br = new BufferedReader(fr)){
							
							String line;
							
							while((line = br.readLine()) != null) {
							
								boolean parameterCharge = line.startsWith("CHG_");
								
								String[] split = line.split(" ");

								String parameterValueAsString = split[split.length - 2];
								String derivValueAsString = split[split.length - 1];
								String name = split[0];
								
								Double derivValue = new Double(derivValueAsString);
								Double parameterValue = new Double(parameterValueAsString);
								// this function is called every time
								Map<String, Object> valuesFromFile2 = Argument.LJ.getValuesFromFile();

								if(parameterCharge) {
									String chargeIndex = split[2];
									Long index = new Long(chargeIndex);
									Deriv e = new Charge(parameterValue, derivValue, property, compound, name, index);
									derivateds.add(e);
									continue;
								}

								String atomTypeIndexAsString = split[1];
								AtomType at1 = (AtomType) valuesFromFile2.get(atomTypeIndexAsString);
								String atomType2IndexAsString = split[2];
								AtomType at2 = (AtomType) valuesFromFile2.get(atomType2IndexAsString);
								Deriv e = new opt.data.deriv.LJ(parameterValue, derivValue, property, compound, name, at1, at2);
								derivateds.add(e);
							}
						
						}catch(IOException e) {
							continue;
						}
						super.put(derivName.toString(), derivateds);
					}
				}
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

	private static String[] splitThisLine(String line) {
		String[] split = line.split(" ");
		List<String> asList = Arrays.asList(split);
		asList = asList.stream().filter(s -> false == s.trim().isEmpty()).collect(Collectors.toList());
		split =  asList.toArray(new String[asList.size()]);
		return split;
	}

	private void putAll(String fileName) {
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			String type = this.name();
			throw new InexistantFileException(fileName, type);
		}
		Properties props = new Properties();
		try {
			props.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected error", e);
		}
		Set<Object> keySet = props.keySet();
		
		for (Object key : keySet) {
			Object value = props.get(key);
			this.put(key.toString(), value);
		}
	}
	

}
