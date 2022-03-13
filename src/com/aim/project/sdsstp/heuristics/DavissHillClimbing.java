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
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random random;
	
	public DavissHillClimbing(Random random) {
	
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		int times = getTimes(depthOfSearch);							// Get the corresponding number of times according to the input DOS.
		int index = 0, range = 0;
		int[] source = new int[solution.getNumberOfLandmarks()];
		int[] perm = new int[solution.getNumberOfLandmarks()];			// The array to store the random sequence of perturbations.
		double candidateValue = 0;
		double currentValue = solution.getObjectiveFunctionValue();
		for (int time = 0; time < times; time++) {
			for (int i = 0; i < solution.getNumberOfLandmarks(); i++) {	// Keep a source array to randomly select from.
				source[i] = i;
			}
			range = solution.getNumberOfLandmarks();					// Initialize the range that can be randomly selected from.
			for (int i = 0; i < solution.getNumberOfLandmarks(); i++) {
				index = random.nextInt(range);							// Choose a random value in the range.
				perm[i] = source[index];								// According to the random value selected above, the corresponding value in the source array is placed in the result array.
				source[index] = source[range-1];						// Replace the selected value in the source array with the last value within the range.
				range--;												// Narrow range.
			}
			for (int i = 0; i < perm.length; i++) {						// Perform a sequence of adjacent swap according to perm[], which is generated above.
				performAdjacentSwap(solution, perm[i]);
				candidateValue = solution.getObjectiveFunctionValue();
				if (candidateValue <= currentValue) {					// Persist swap which results in an improving or equal quality solution.
					currentValue = candidateValue;
				} else {												// Otherwise, swap back to the previous solution.
					performAdjacentSwap(solution, perm[i]);
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
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return true;
	}
}
