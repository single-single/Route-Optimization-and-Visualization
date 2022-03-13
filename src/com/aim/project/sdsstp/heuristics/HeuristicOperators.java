package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 */
public class HeuristicOperators {

	/*
	 *  PLEASE NOTE THAT USE OF THIS CLASS IS OPTIONAL BUT WE
	 *  STRONGLY RECOMMEND THAT YOU IMPLEMENT ANY COMMON FUNCTIONALITY
	 *  IN HERE TO HELP YOU WITH IMPLEMENTING THE HEURISTICS.
	 *
	 *  (HINT: It might be worthwhile to have a method that performs adjacent swaps in here :)) 
	 */

	private static final boolean ENABLE_CHECKING = false;

	private ObjectiveFunctionInterface obj;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.obj = f;
	}

	/* This function returns the corresponding number of times according to the input IOM or DOS. */
	protected int getTimes(double i) {
		
		int times = 0;
		if (0.0 <= i && i < 0.2) {
			times = 1;
		} else if (0.2 <= i && i < 0.4) {
			times = 2;
		} else if (0.4 <= i && i < 0.6) {
			times = 3;
		} else if (0.6 <= i && i < 0.8) {
			times = 4;
		} else if (0.8 <= i && i < 1.0) {
			times = 5;
		} else {
			times = 6;
		}
		return times;
	}
	
	/* This function takes a solution and the position to be swapped as parameters,
	 * and swap the element at that position with the element after it. */
	protected void performAdjacentSwap(SDSSTPSolutionInterface solution, int var_a) {

		int[] currentRep = solution.getSolutionRepresentation().getSolutionRepresentation();	// Get the current solution representation.
		int[] previousRep = currentRep.clone();						// Save the solution representation before swap for use in delta evaluation.
		if (currentRep.length < 2 || var_a >= currentRep.length) {  // Handle exceptions.
			System.out.println("Perform adjacent swap failed.");
			return;
		}
		int temp = currentRep[var_a];
		if (var_a == currentRep.length - 1) {						// If the selected position is the last one of permutation, swap with the first one.
			currentRep[var_a] = currentRep[0];
			currentRep[0] = temp;
		} else {													// Otherwise, swap with the next element.
			currentRep[var_a] = currentRep[var_a + 1];
			currentRep[var_a + 1] = temp;
		}
		deltaEvaluation(solution, var_a, previousRep);				// Use delta evaluation to update the objective function value.
	}
	
	/* This is a delta evaluation function specially designed for adjacent swap.
	 * It takes a solution, the position to be swapped and the permutation before the
	 * swap as parameters, calculates the difference between the objective function
	 * value before and after the exchange, and updates the new objective function
	 * value to the sum of the original objective function value and delta. */
	protected void deltaEvaluation(SDSSTPSolutionInterface solution, int index, int[] perm) {
		
		double delta = 0;
		if (perm.length == 2) {										// Handle the special case where the permutation length is 2.
			delta += obj.getTravelTimeFromTourOfficeToLandmark(perm[1]);
			delta += obj.getTravelTime(perm[1], perm[0]);
			delta += obj.getTravelTimeFromLandmarkToTourOffice(perm[0]);
			delta -= obj.getTravelTimeFromTourOfficeToLandmark(perm[0]);
			delta -= obj.getTravelTime(perm[0], perm[1]);
			delta -= obj.getTravelTimeFromLandmarkToTourOffice(perm[1]);
		} else if (perm.length == 3) {								// Handle the special case where the permutation length is 3.
			if (index == 0) {										// The first element is swapped with the second element.
				delta += obj.getTravelTimeFromTourOfficeToLandmark(perm[1]);
				delta += obj.getTravelTime(perm[1], perm[0]);
				delta += obj.getTravelTime(perm[0], perm[2]);
				delta -= obj.getTravelTimeFromTourOfficeToLandmark(perm[0]);
				delta -= obj.getTravelTime(perm[0], perm[1]);
				delta -= obj.getTravelTime(perm[1], perm[2]);
			} else if (index == 1) {								// The second element is swapped with the last element.
				delta += obj.getTravelTime(perm[0], perm[2]);
				delta += obj.getTravelTime(perm[2], perm[1]);
				delta += obj.getTravelTimeFromLandmarkToTourOffice(perm[1]);
				delta -= obj.getTravelTime(perm[0], perm[1]);
				delta -= obj.getTravelTime(perm[1], perm[2]);
				delta -= obj.getTravelTimeFromLandmarkToTourOffice(perm[2]);
			} else {												// The last element is swapped with the first element.
				delta += obj.getTravelTimeFromTourOfficeToLandmark(perm[2]);
				delta += obj.getTravelTime(perm[2], perm[1]);
				delta += obj.getTravelTime(perm[1], perm[0]);
				delta += obj.getTravelTimeFromLandmarkToTourOffice(perm[0]);
				delta -= obj.getTravelTimeFromTourOfficeToLandmark(perm[0]);
				delta -= obj.getTravelTime(perm[0], perm[1]);
				delta -= obj.getTravelTime(perm[1], perm[2]);
				delta -= obj.getTravelTimeFromLandmarkToTourOffice(perm[2]);
			}
		} else {													// Normal case.
			if (index == 0) {										// The first element is swapped with the second element.
				delta += obj.getTravelTimeFromTourOfficeToLandmark(perm[index+1]);
				delta += obj.getTravelTime(perm[index+1], perm[index]);
				delta += obj.getTravelTime(perm[index], perm[index+2]);
				delta -= obj.getTravelTimeFromTourOfficeToLandmark(perm[index]);
				delta -= obj.getTravelTime(perm[index], perm[index+1]);
				delta -= obj.getTravelTime(perm[index+1], perm[index+2]);
			} else if (index == perm.length - 1) {					// The last element is swapped with the first element.
				delta += obj.getTravelTimeFromTourOfficeToLandmark(perm[index]);
				delta += obj.getTravelTime(perm[index], perm[1]);
				delta += obj.getTravelTime(perm[index-1], perm[0]);
				delta += obj.getTravelTimeFromLandmarkToTourOffice(perm[0]);
				delta -= obj.getTravelTimeFromTourOfficeToLandmark(perm[0]);
				delta -= obj.getTravelTime(perm[0], perm[1]);
				delta -= obj.getTravelTime(perm[index-1], perm[index]);
				delta -= obj.getTravelTimeFromLandmarkToTourOffice(perm[index]);
			} else if (index == perm.length - 2) {					// The penultimate element is swapped with the last element.
				delta += obj.getTravelTime(perm[index-1], perm[index+1]);
				delta += obj.getTravelTime(perm[index+1], perm[index]);
				delta += obj.getTravelTimeFromLandmarkToTourOffice(perm[index]);
				delta -= obj.getTravelTime(perm[index-1], perm[index]);
				delta -= obj.getTravelTime(perm[index], perm[index+1]);
				delta -= obj.getTravelTimeFromLandmarkToTourOffice(perm[index+1]);
			} else {												// Swaps that does not involve the first and last elements.
				delta += obj.getTravelTime(perm[index-1], perm[index+1]);
				delta += obj.getTravelTime(perm[index+1], perm[index]);
				delta += obj.getTravelTime(perm[index], perm[index+2]);
				delta -= obj.getTravelTime(perm[index-1], perm[index]);
				delta -= obj.getTravelTime(perm[index], perm[index+1]);
				delta -= obj.getTravelTime(perm[index+1], perm[index+2]);
			}
		}
		double previousValue = solution.getObjectiveFunctionValue();
		double currentValue = previousValue + delta;
		solution.setObjectiveFunctionValue(currentValue);
	}
	
}
