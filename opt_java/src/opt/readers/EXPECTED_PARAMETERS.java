package opt.readers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import opt.exceptions.IncorrectFileTypeException;
import opt.exceptions.InexistanttFileException;
import opt.utils.Validations;

public 
enum EXPECTED_PARAMETERS{
	
	H {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	CONF {

		@Override
		void read(String fileName) {
			
			
		}
	},
	PARAM {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	DATA {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	PROP {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	DERIV {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	LJ {
		@Override
		void read(String fileName) {
			// TODO Auto-generated method stub
			
		}
	},
	OUT {
		@Override
		void read(String fileName) {
			
			
			
		}
		
		@Override
		public boolean requiresExistingFile() {
			return false;
		}
	}

	;
	protected boolean created;
	
	public boolean isCreated() {
		return this.created;
	}
	
	
	private boolean matches(String paramType) {
	
		String string = "-" + this.toString();
		boolean matches = string.equalsIgnoreCase(paramType);
		return matches;
	}
	
	public static boolean readAnyFile(String paramType, String fileName) {
		
		EXPECTED_PARAMETERS[] values = EXPECTED_PARAMETERS.values();
		
		for (EXPECTED_PARAMETERS expected : values) {
			boolean notMatches = false == expected.matches(paramType);
			
			if(notMatches) {
				// esse tipo de parametro nao foi capaz de le-lo
				continue;
			}
			expected.created = true;
			Validations.notEmptyParameter("fileName", fileName);
			
			boolean doesNotRequireExistingFile = false == expected.requiresExistingFile();
		
			if(doesNotRequireExistingFile) {
				expected.read(fileName);
				continue;
			}
			validateExistentFile(fileName);
			expected.read(fileName);
			
			// o metodo morre aqui
			return true;
		}
		// significa que passou por todos e nenhum dos tipos de parametro foi capaz de le-lo
		return false;
	}
	
	private static void validateExistentFile(String fileName) {
		
		File f = new File(fileName);
		
		boolean doesNotExist = false == f.exists();
		if(doesNotExist) {
			throw new InexistanttFileException(fileName);
		}
		
		boolean directory = f.isDirectory();
		
		if(directory) {
			throw new IncorrectFileTypeException(fileName);
		}
		
	}


	abstract void read(String fileName);
	
	public String getStatus() {
		Map<String, Object> props = new LinkedHashMap<>();
		props.put("parameter", this.name());
		props.put("created", this.created);
		return props.toString();
	}
	
	public boolean requiresExistingFile() {
		return true;
	}
}
