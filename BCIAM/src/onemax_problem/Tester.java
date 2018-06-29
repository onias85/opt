package onemax_problem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;
import metaheurictics.strategy.Strategy;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.MultiGenerator;
import metaheuristics.generators.SimulatedAnnealing;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class Tester {
	
	Problem problem;
	int countmaxIterations;
	int operatornumber = 1;

	public void configOnemaxProblemOneMax(){
		// Initialization and assignment problem classes
		this.problem = new Problem();
		OnemaxMutationOperator operator = new OnemaxMutationOperator();
		OnemaxObjetiveFunction objFunction = new OnemaxObjetiveFunction();
		OnemaxCodification codification = new OnemaxCodification();

		this.problem.setCodification(codification);
		this.problem.setFunction(objFunction);
		this.problem.setOperator(operator);
		this.problem.setTypeProblem(ProblemType.Maximizar);
		this.problem.setState(new State());
		this.problem.setPossibleValue(2);
	}

	
	public void configPortfolio() {
		EvolutionStrategies.countRef = 50;
		EvolutionStrategies.truncation = 20;
		EvolutionStrategies.PM = 0.9;
	 	
		GeneticAlgorithm.countRef = 50; //cantidad de individuos
		GeneticAlgorithm.truncation = 20;
		GeneticAlgorithm.PM = 0.5;
		GeneticAlgorithm.PC = 0.9;
		
		DistributionEstimationAlgorithm.countRef = 50;
		DistributionEstimationAlgorithm.truncation = 20;	
	
		SimulatedAnnealing.alpha = 0.93;
    	SimulatedAnnealing.tinitial = 20.0;
    	SimulatedAnnealing.tfinal = 0.0;
    	SimulatedAnnealing.countIterationsT = 50;
	}
	
	public void executePortfolio( float severity, int period) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		countmaxIterations = period * 100;
		Onemax.period =  period;
		Onemax.severity = severity;
		Onemax.cantActual = period;
		configOnemaxProblemOneMax();
		configPortfolio();
		Strategy.getStrategy().setStopexecute(new StopExecute());
		Strategy.getStrategy().setUpdateparameter(new UpdateParameter());
		Strategy.getStrategy().setProblem(this.problem);
		this.problem.setRef(list.get(0));
		Strategy.getStrategy().executeStrategy(countmaxIterations, operatornumber, GeneratorType.MultiGenerator);
		Strategy.destroyExecute();
		MultiGenerator.destroyMultiGenerator();
		System.out.println("Corrida: " + i);
	}
	
	
	

}
