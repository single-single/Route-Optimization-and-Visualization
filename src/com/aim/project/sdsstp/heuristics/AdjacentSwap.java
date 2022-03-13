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
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random random;

	public AdjacentSwap(Random random) {

		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		int var_a = 0;
		int times = 0;							// Get the corresponding number of times according to the input IOM.
		if (0.0 <= intensityOfMutation && intensityOfMutation < 0.2) {
			times = 1;
		} else if (0.2 <= intensityOfMutation && intensityOfMutation < 0.4) {
			times = 2;
		} else if (0.4 <= intensityOfMutation && intensityOfMutation < 0.6) {
			times = 4;
		} else if (0.6 <= intensityOfMutation && intensityOfMutation < 0.8) {
			times = 8;
		} else if (0.8 <= intensityOfMutation && intensityOfMutation < 1.0) {
			times = 16;
		} else {
			times = 32;
		}
		for (int i = 0; i < times; i++) {		// Perform times of adjacent swaps at random points.
			var_a = random.nextInt(solution.getNumberOfLandmarks());
			performAdjacentSwap(solution, var_a);
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

