package com.aim.project.sdsstp.hyperheuristics;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.aim.project.sdsstp.AIM_SDSSTP;
import com.aim.project.sdsstp.SolutionPrinter;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 */
public class SR_IE_HH extends HyperHeuristic {
	
	public SR_IE_HH(long seed) {
		
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {

		problem.setMemorySize(3);
		
		problem.initialiseSolution(0);
		problem.initialiseSolution(1);
		double current = problem.getFunctionValue(0);
		
		problem.setIntensityOfMutation(0.2);
		problem.setDepthOfSearch(0.2);
		
		int h = 1;
		boolean accept;
		
		int[] xos = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i : xos) {
			set.add(i);
		}
		
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		int iteration = 1;
		while(!hasTimeExpired()) {
			
			h = rng.nextInt(problem.getNumberOfHeuristics());
			double candidate;
			
			if(set.contains(h)) {
				
				problem.initialiseSolution(2);
				candidate = problem.applyHeuristic(h, 0, 2, 1);
				
			} else {
				
				candidate = problem.applyHeuristic(h, 0, 1);
			}
			
			accept = candidate <= current;
			if(accept) {
				
				System.out.println(iteration + "\t" + problem.getFunctionValue(0) + "\t" + problem.getFunctionValue(1) + "\ttrue");
				problem.copySolution(1, 0);
				current = candidate;
				
			} else {
				
				System.out.println(iteration + "\t" + problem.getFunctionValue(0) + "\t" + problem.getFunctionValue(1) + "\tfalse");
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

	@Override
	public String toString() {

		return "SR_IE_HH";
	}
}
