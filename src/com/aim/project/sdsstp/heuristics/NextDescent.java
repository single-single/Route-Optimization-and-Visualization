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
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random random;
	
	public NextDescent(Random random) {
	
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		int times = getTimes(depthOfSearch);						// Get the corresponding number of times according to the input DOS.
		int improvements = 0;										// The variable used to record the number of accepted improvements.
		boolean isLoop = false;										// The variable used to indicate whether a complete loop of the solution has completed without finding any improvement.
		double candidateValue = 0;
		double currentValue = solution.getObjectiveFunctionValue();
		int startPoint = random.nextInt(solution.getNumberOfLandmarks());	// Pick a random starting point.
		int currentPoint = startPoint;								// The current landmark to be swapped.
		while (true) {
			if (improvements >= times || isLoop) {					// The algorithm stops when one of the two conditions is met.
				break;
			}
			performAdjacentSwap(solution, currentPoint);
			candidateValue = solution.getObjectiveFunctionValue();
			if (candidateValue < currentValue) {					// If there is an improvement.
				improvements++;										// Update the number of accepted improvements.
				currentValue = candidateValue;						// Update the best objective function value found.
				if (currentPoint == solution.getNumberOfLandmarks() - 1) {	// Set the starting point to the next point and re-record the loop without finding any improvement.
					startPoint = 0;
				} else {
					startPoint = currentPoint + 1;
				}
				currentPoint = startPoint;							// Update the current landmark to be swapped.
				continue;
			} else {												// Otherwise, swap back to the previous solution.
				performAdjacentSwap(solution, currentPoint);
			}
			if (currentPoint == solution.getNumberOfLandmarks() - 1) {	// Update the current landmark to be swapped.
				currentPoint = 0;
			} else {
				currentPoint++;
			}
			if (currentPoint == startPoint) {						// If a complete loop of the solution has completed without finding any improvement, break in the next loop.
				isLoop = true;
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
