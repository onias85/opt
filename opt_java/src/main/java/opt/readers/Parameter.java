package opt.readers;

import java.io.File;
import java.util.Arrays;

import opt.exceptions.EmptyParametersException;
import opt.exceptions.IncorrectFileTypeException;
import opt.exceptions.InexistanttFileException;
import opt.exceptions.InvalidSequenceParametersException;

public 
enum Parameter{
		
	CONF {
		@Override
		void read(String fileName) {
			System.out.println("reading a conf file called  " + fileName);
			
		}
	},
	LJ {
		@Override
		void read(String fileName) {
			System.out.println("reading a LJ file called  " + fileName);
			
		}
	},
	PARAM {
		@Override
		void read(String fileName) {
			System.out.println("reading a param file called  " + fileName);
			
		}
	},
	DATA {
		@Override
		void read(String fileName) {
			System.out.println("reading a data file called  " + fileName);
			
		}
	},
	PROP {
		@Override
		void read(String fileName) {
			System.out.println("reading a prop file called  " + fileName);
			
		}
	},
	DERIV {
		@Override
		void read(String fileName) {
		
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
				throw new InexistanttFileException(fileName, myName);
			}
			
			boolean directory = file.isDirectory();
			
			if(directory) {
				String.format("Reading a folder called '%s' containing the files '%s'", fileName, Arrays.asList(file.list()));
				return;
			}
			
			System.out.println("Reading a single file Called " + fileName);
		}

		@Override
		public boolean readsJustASingleFile() {
			return false;
		}
	},
	OUT {
		@Override
		void read(String fileName) {
			
			System.out.println("writing on a out file called  " + fileName);
			
		}
		
		@Override
		public boolean readsJustASingleFile() {
			return false;
		}
	}, 
	H {
		@Override
		void read(String fileName) {
			
		}
	},

	HELP{

		@Override
		void read(String fileName) {
			
		}
		
	}
	;
	
	public boolean matches(String paramType) {
	
		String string = "-" + this.toString();
		boolean matches = string.equalsIgnoreCase(paramType);
		return matches;
	}
	
	private static void validateExistentFile(String fileName, String type) {
		
		File f = new File(fileName);
		
		boolean doesNotExist = false == f.exists();
		if(doesNotExist) {
			throw new InexistanttFileException(fileName, type);
		}
		
		boolean directory = f.isDirectory();
		
		if(directory) {
			throw new IncorrectFileTypeException(fileName);
		}
	}

	abstract void read(String fileName);
	
	public boolean readsJustASingleFile() {
		return true;
	}
	
	public static void readAllFilesInParameters(String... args) {
		
		if(args == null) {
			throw new EmptyParametersException();
		}
		
		int argumentsCount = args.length;
		
		if(argumentsCount == 0) {
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
		
		
		Parameter[] values = values();

		// we use "-2" because we are ignoring "-H" and "-Help", so we multiply by to "*2" because the parameters 
		// come in pair, being the first one the type of parameter and the second one the file of parameter
		int validParametersCount = (values.length - 2) * 2;
		
		for(int k = 0, m = 0; k < validParametersCount; k++, m += 2) {
			
			Parameter parameter = values[k];
			
			String actualType = args[m];
			String expectedType = parameter.name();
			boolean doesNotMatch = false == parameter.matches(actualType);
			
			if(doesNotMatch) {
				throw new InvalidSequenceParametersException(expectedType, actualType);
			}

			String filePathToRead = args[m + 1];
			
			boolean doesNotReadOneFile = false == parameter.readsJustASingleFile();
			
			if(doesNotReadOneFile) {
				parameter.read(filePathToRead);
				continue;
			}
			
			validateExistentFile(filePathToRead, expectedType);
			parameter.read(filePathToRead);

		}
		
		
	}
	
}
