package onemax_problem;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.Operator;
import problem.definition.State;

public class OnemaxMutationOperator extends Operator {

	private ArrayList<Integer> mascara = new ArrayList<Integer>();
	
	@Override
	public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
		List<State> listNeigborhood = new ArrayList<State>();
		for (int i = 0; i < operatornumber; i++){
			int key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			int candidate = 1 -  stateCurrent.getCode().get(key);
			State state = new State();
			state.setCode(new ArrayList<Integer>(stateCurrent.getCode()));
			state.getCode().set(key, candidate);
			listNeigborhood.add(state);
		}
		return listNeigborhood;
	}

	@Override
	public List<State> generateRandomState(Integer operatornumber) {
		List<State> list = new ArrayList<State>();
		for (int i = 0; i < operatornumber; i++) {
			State stateinitial = new State();
			for (int j = 0; j < Onemax.countVariables; j++) {
				int candidate = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue(j);
				stateinitial.getCode().add(candidate);
			} 
			list.add(stateinitial);
		}
		return list;
	}

	@Override
	public int getCountD() {
		// TODO Auto-generated method stub
		return Onemax.cantActual;
	}

	@Override
	public int getPeriod() {
		// TODO Auto-generated method stub
		return Onemax.period;
	}

	@Override
	public State newRef(State refOld) {
		// TODO Auto-generated method stub
		mascara = getMascara();
		State state = new State();
		state.setEvaluation(refOld.getEvaluation());
		state.setCode(new ArrayList<Integer>(refOld.getCode()));
		state.setNumber(refOld.getNumber());
		state.setTypeGenerator(refOld.getTypeGenerator());
		for (int i = 0; i < refOld.getCode().size(); i++) {
			if(mascara.get(i) == 1){
				state.getCode().set(i, 1 - state.getCode().get(i));
			}
		}
		mascara.clear();
		/*int countPos = (int) (Onemax.countVariables * Onemax.severity);
		State state = new State();
		state.setEvaluation(refOld.getEvaluation());
		state.setCode(new ArrayList<Integer>(refOld.getCode()));
		state.setNumber(refOld.getNumber());
		state.setTypeGenerator(refOld.getTypeGenerator());
		for (int j = 0; j < countPos; j++) {
			int key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			int value = state.getCode().get(key);
			state.getCode().set(key, 1 - value);
		} */
		return state;
	}
   
	//generar mascara aleatoria, la cantidad de unos en la mascara depende de la severidad
	public ArrayList<Integer> getMascara() {
		for (int i = 0; i < Onemax.countVariables; i++) {
			mascara.add(0);
		}
		int count = 0;
		int countS = (int) (Onemax.countVariables * Onemax.severity);
		while (count < countS) {
			int r = (int)(Math.random() * (double)(Onemax.countVariables));
			if(mascara.get(r)==0){
				mascara.set(r, 1);
				count++;
			}
		}
		return mascara;
	}

	public void setMascara(ArrayList<Integer> mascara) {
		this.mascara = mascara;
	}
	
	public void generateNewMask() {
	}

}
