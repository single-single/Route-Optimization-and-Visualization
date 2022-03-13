package com.aim.project.sdsstp.hyperheuristics;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.aim.project.sdsstp.AIM_SDSSTP;
import com.aim.project.sdsstp.SolutionPrinter;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * @author Shiqi XIN
 *
 * This is my own hyper-heuristic.
 * It uses an improved version of reinforcement learning as the heuristic selection
 * mechanism, and uses a simulated annealing algorithm for move acceptance.
 * Reinforcement learning is for a heuristic pair instead of a single heuristic.
 * It scores each heuristic pair and selects the one with the highest score to perform
 * each time. It is worth mentioning that two mechanisms are used here to avoid stuck.
 * One is that when choosing a heuristic pair, there will be a certain probability
 * of randomly choosing instead of choosing the highest one. In addition, when the 
 * result exceeds 10 consecutive times without improvement, the temperature will be reheat
 * to accept more non-improve results and the scores of all heuristic pairs will be reset.
 * IOM and DOS are change dynamically as it runs.
 */
public class RL_SA_HH extends HyperHeuristic {
	
	public HeuristicPair[] heuristicPairs;
	public LinkedHashMap<HeuristicPair, Integer> heuristicScores;
	public int upperBound;
	public int lowerBound;
	public int defaultScore;
	public double currentTemperature;

	public RL_SA_HH(long seed) {
		
		super(seed);
	}
	
	@Override
	protected void solve(ProblemDomain problem) {

		problem.setMemorySize(3);
		
		problem.initialiseSolution(0);
		problem.initialiseSolution(1);
		double current = problem.getFunctionValue(0);
		double candidate = problem.getFunctionValue(1);
		
		double iom = 0.2;
		double dos = 0.2;
		problem.setIntensityOfMutation(iom);
		problem.setDepthOfSearch(dos);
		
		int[] xos = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i : xos) {
			set.add(i);
		}
		
		int stuckTimes = 0;
		
		upperBound = 20;															// Initialize the upper bound, lower bound, default score, and temperature for annealing.
		lowerBound = 1;
		defaultScore = 10;
		currentTemperature = current;
		
		final int[] mtns = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		final int[] lss = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		
		int pairIndex = 0;
		heuristicPairs = new HeuristicPair[mtns.length * lss.length];				// Store all heuristic pairs in an array.
		for (int i = 0; i < mtns.length; i++) {
			for (int j = 0; j < lss.length; j++) {
				
				heuristicPairs[pairIndex] = new HeuristicPair(mtns[i], lss[j]);
				pairIndex++;
			}
		}
		
		heuristicScores = new LinkedHashMap<HeuristicPair, Integer>();				// Initialize the scores of all heuristic pairs.
		for (HeuristicPair hp : heuristicPairs) {
			
			heuristicScores.put(hp, defaultScore);
		}
		
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");
		
		int iteration = 1;
		while(!hasTimeExpired()) {
			
			HeuristicPair hp = null;
			if (rng.nextDouble() < 0.01) {											// There is a 1% probability of randomly choosing a heuristic pair.
				
				hp = heuristicPairs[rng.nextInt(heuristicPairs.length)];
				
			} else {																// Select the heuristic pair with the highest score to execute.
				
				hp = selectMaxScore();
			}
			
			if (getScore(hp) > defaultScore) {										// Increase the number of runs of the heuristic pair with high scores.
				
				iom = Math.min(1.0, iom + 0.01 * (getScore(hp) - defaultScore));
				dos = Math.min(1.0, dos + 0.01 * (getScore(hp) - defaultScore));
				problem.setIntensityOfMutation(iom);
				problem.setDepthOfSearch(dos);
				
			} else {																// Decrease the number of runs of the heuristic pair with low scores.
				
				iom = Math.max(0.0, iom - 0.1 * (defaultScore - getScore(hp)));
				dos = Math.max(0.0, dos - 0.1 * (defaultScore - getScore(hp)));
				problem.setIntensityOfMutation(iom);
				problem.setDepthOfSearch(dos);
			}
			
			int h1 = hp.getFirst();
			int h2 = hp.getLast();
			
			if(set.contains(h1)) {													// Run the mutation heuristic.
				
				problem.initialiseSolution(2);
				problem.applyHeuristic(h1, 0, 2, 1);
				
			} else {
				
				problem.applyHeuristic(h1, 0, 1);
			}
			
			candidate = problem.applyHeuristic(h2, 1, 1);							// Run the local search heuristic.
			
			double delta = candidate - current;
			
			if(delta < 0 || rng.nextDouble() < Math.exp(-1.0 * delta / currentTemperature)) {
				
				System.out.println(iteration + "\t" + problem.getFunctionValue(0) + "\t" + problem.getFunctionValue(1) + "\ttrue");
				problem.copySolution(1, 0);
				current = candidate;
				stuckTimes = 0;
				
			} else {
				
				System.out.println(iteration + "\t" + problem.getFunctionValue(0) + "\t" + problem.getFunctionValue(1) + "\tfalse");
				stuckTimes++;
			}
			
			if (delta < 0) {														// Increase or decrease scores based on whether there is an improvement.
				
				incrementScore(hp);
				
			} else {
				
				decrementScore(hp);
			}
			
			if (stuckTimes > 10) {					// If there is no improvement for more than 10 consecutive times, reheat the temperature and reset all scores.
				
				stuckTimes = 0;
				reheatTemperature();
				for (Entry<HeuristicPair, Integer> entry : heuristicScores.entrySet()) {
					
					entry.setValue(defaultScore);
				}
				
			} else {								// Otherwise, cool down.
				
				advanceTemperature();
			}
			
			iteration++;
		}
		
		int[] cities = ((AIM_SDSSTP) problem).getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		List<SDSSTPLocation> routeLocations = new ArrayList<>();
		
		for(int i = 0; i < ((AIM_SDSSTP) problem).getBestSolution().getNumberOfLandmarks(); i++) {
			routeLocations.add(((AIM_SDSSTP) problem).instance.getLocationForLandmark(cities[i]));
		}
		
		SDSSTPSolutionInterface oSolution = ((AIM_SDSSTP) problem).getBestSolution();
		SolutionPrinter.printSolution(((AIM_SDSSTP) problem).instance.getSolutionAsListOfLocations(oSolution));
		
	}
	
	/* The function used to obtain the score of the specified heuristic pair. */
	public int getScore(HeuristicPair h) {
		
		if (heuristicScores.get(h) == null) {
			
			return 0;
			
		} else {
			
			return heuristicScores.get(h);
		}
	}
	
	/* Increase scores to the specified heuristic pair. */
	public void incrementScore(HeuristicPair h) {
		
		if (getScore(h) < upperBound) {
			
			heuristicScores.put(h, getScore(h) + 1);
		}
	}
	
	/* Decrease scores to the specified heuristic pair. */
	public void decrementScore(HeuristicPair h) {
		
		if (getScore(h) > lowerBound) {
			
			heuristicScores.put(h, getScore(h) - 1);
		}
	}
	
	/* Choose the one with the highest score from all heuristic pairs.
	 * If there are several heuristic pairs with the same highest score,
	 * randomly select one from them. */
	public HeuristicPair selectMaxScore() {
		
		int tempMaxScore = 0;
		HeuristicPair chosenHeuristic = null;
		
		for (HeuristicPair hp : heuristicPairs) {
			
			if (getScore(hp) > tempMaxScore) {
				
				chosenHeuristic = hp;
				tempMaxScore = getScore(hp);
				
			} else {
				
				if (getScore(hp) == tempMaxScore && rng.nextBoolean()) {
					chosenHeuristic = hp;
				}
			}
		}
		
		return chosenHeuristic;
	}
	
	/* Use Lundy Mees to decrease the temperature. */
	public void advanceTemperature() {
		
		final double beta = 0.0001;
		currentTemperature = currentTemperature / (1 + beta * currentTemperature);
	}
	
	/* Reheat the temperature by 1% */
	public void reheatTemperature() {
		
		final double beta = 0.01;
		currentTemperature = currentTemperature * (1 + beta);
	}
	
	@Override
	public String toString() {

		return "RL_SA_HH";
	}
	
}
