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
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random random;
	
	public Reinsertion(Random random) {

		super();
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		int var_a = 0, var_b = 0;
		int times = getTimes(intensityOfMutation);				// Get the corresponding number of times according to the input IOM.
		for (int i = 0; i < times; i++) {
			do {												// Randomly select two non-duplicate points.
				var_a = random.nextInt(solution.getNumberOfLandmarks());	// The landmark location selected to be reinserted.
				var_b = random.nextInt(solution.getNumberOfLandmarks());	// The position selected to be inserted into.
			} while (var_a == var_b);
			if (var_a < var_b) {
				for (int j = var_a; j < var_b; j++) {			// Swap the point to be reinserted step by step forward to the specified position.
					performAdjacentSwap(solution, j);
				}
			} else {
				for (int j = var_a - 1; j >= var_b; j--) {		// Swap the point to be reinserted step by step back to the specified position.
					performAdjacentSwap(solution, j);
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
