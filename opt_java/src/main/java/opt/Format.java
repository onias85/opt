package opt;

import opt.utils.Ask;

public enum Format {

	DOUBLE {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isDouble(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {

			return new Double(str);
		}
	},
	LONG {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isLong(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {

			return new Long(str);
		}
	},
	BOOLEAN {
		@Override
		boolean isCompatible(String str) {
			boolean isCompatible = Ask.isBoolean(str);
			return isCompatible;
		}

		@Override
		Object parse(String str) {
			if ("true".equalsIgnoreCase(str)) {
				return true;
			}
			return false;
		}
	},
	STRING {
		@Override
		boolean isCompatible(String str) {
			return true;
		}

		@Override
		Object parse(String str) {

			return str;
		}
	};

	abstract boolean isCompatible(String str);

	abstract Object parse(String str);
}