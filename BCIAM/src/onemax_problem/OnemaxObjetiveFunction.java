package onemax_problem;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.State;

public class OnemaxObjetiveFunction extends ObjetiveFunction {

	@Override
	public Double Evaluation(State state) {
		double count = 0;
		for (int i = 0; i < state.getCode().size(); i++) {
			if(Strategy.getStrategy().getProblem().getRef().getCode().get(i) == state.getCode().get(i))
				count++;
		}
		return count;
	}
}
