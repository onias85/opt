package onemax_problem;

import problem.definition.Codification;
import problem.definition.State;

public class OnemaxCodification extends Codification {

	@Override
	public int getAleatoryKey() {
		return (int)(Math.random() * (double)(Onemax.countVariables));
	}

	@Override
	public int getSpaceSearchSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVariableAleatoryValue(int variable) {
		// TODO Auto-generated method stub
		return (int)(Math.random() * (double)(2));
	}

	@Override
	public int getVariableCount() {
		// TODO Auto-generated method stub
		return Onemax.countVariables;
	}

	@Override
	public int getVariableDomain(int variable) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validState(State state) {
		boolean encontrar = true;
		int i = 0;
		while (encontrar && i < state.getCode().size()) {
			if(state.getCode().get(i) != 0 || state.getCode().get(i) != 1)
				encontrar = false;
			else i++;
		}
		return encontrar;
	}

}
