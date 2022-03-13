package com.aim.project.sdsstp.heuristics;

import java.util.Random;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation)
 * - public boolean isCrossover()
 * - public boolean usesIntensityOfMutation()
 * - public boolean usesDepthOfSearch()
 */
public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random random;
	
	public InversionMutation(Random random) {
	
		super();
		
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		int var_a = 0, var_b = 0;
		int times = getTimes(intensityOfMutation);			// Get the corresponding number of times according to the input IOM.
		for (int i = 0; i < times; i++) {
			do {											// Randomly select two non-duplicate points.
				var_a = random.nextInt(solution.getNumberOfLandmarks());	// The start point of inversion.
				var_b = random.nextInt(solution.getNumberOfLandmarks());	// The end point of inversion.
			} while (var_a == var_b);
			if (var_a > var_b) {							// Let the start point less than the end point.
				int temp = var_a;
				var_a = var_b;
				var_b = temp;
			}
			for (int j = var_b; j > var_a; j--) {			// By swapping each point to the corresponding position, the reversal of the route between the two locations is achieved.
				for (int h = var_a; h < j; h++) {
					performAdjacentSwap(solution, h);
				}
			}
		}
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return false;
	}

}
