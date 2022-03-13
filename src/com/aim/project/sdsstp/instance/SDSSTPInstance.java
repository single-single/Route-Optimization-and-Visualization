package com.aim.project.sdsstp.instance;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;
import com.aim.project.sdsstp.solution.SDSSTPSolution;
import com.aim.project.sdsstp.solution.SolutionRepresentation;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public SDSSTPSolution createSolution(InitialisationMode mode)
 */
public class SDSSTPInstance implements SDSSTPInstanceInterface {

	private final String strInstanceName;

	private final int iNumberOfLandmarks;

	private final SDSSTPLocation oTourOffice;

	private final SDSSTPLocation[] aoLandmarks;

	private final Random oRandom;

	private final ObjectiveFunctionInterface oObjectiveFunction;

	public SDSSTPInstance(String strInstanceName, int iNumberOfLandmarks,
			com.aim.project.sdsstp.instance.SDSSTPLocation oTourOffice,
			com.aim.project.sdsstp.instance.SDSSTPLocation[] aoLandmarks, Random oRandom,
			ObjectiveFunctionInterface f) {

		this.strInstanceName = strInstanceName;
		this.iNumberOfLandmarks = iNumberOfLandmarks;
		this.oTourOffice = oTourOffice;
		this.aoLandmarks = aoLandmarks;
		this.oRandom = oRandom;
		this.oObjectiveFunction = f;
	}

	@Override
	public SDSSTPSolution createSolution(InitialisationMode mode) {

		// TODO
		int[] perm = new int[iNumberOfLandmarks];					// The array to store the result.
		
		if (mode == InitialisationMode.RANDOM) {
			
			int[] source = new int[iNumberOfLandmarks];				// Keep a source array to randomly select from.
			for (int i = 0; i < iNumberOfLandmarks; i++) {
				source[i] = i;
			}
			int index = 0;											// Initialize the index used for random selection.
			int range = iNumberOfLandmarks;							// Initialize the range that can be randomly selected from.
			for (int i = 0; i < iNumberOfLandmarks; i++) {			// Traverse the array and assign each element a random value that hasn't appeared before.
				index = oRandom.nextInt(range);						// Choose a random value in the range.
				perm[i] = source[index];							// According to the random value selected above, the corresponding value in the source array is placed in the result array.
				source[index] = source[range-1];					// Replace the selected value in the source array with the last value within the range.
				range--;											// Narrow range.
			}
			
		} else if (mode == InitialisationMode.CONSTRUCTIVE) {
			
			int timeCurr, timeCand;
			ArrayList<Integer> indices = new ArrayList<>();			// Used to store the indices of all the closest landmarks.
			for (int i = 0; i < iNumberOfLandmarks; i++) {			// Traverse the array and assign a value for each element.
				if (i == 0) {										// The first element is listed separately because it needs to calculate the traveling time from the office to it.
					timeCurr = Integer.MAX_VALUE;
					for (int j = 0; j < iNumberOfLandmarks; j++) {	// Traverse the array to find the smallest traveling time from the office.
						timeCand = oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(j);
						if (timeCand < timeCurr) {					// If the travel time of the current landmark is less than timeCurr, update timeCurr.
							timeCurr = timeCand;
						}
					}
					for (int j = 0; j < iNumberOfLandmarks; j++) {	// Traverse the array to find landmarks whose traveling time is the smallest.
						if (oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(j) == timeCurr) {
							indices.add(j);
						}
					}
					perm[i] = indices.get(oRandom.nextInt(indices.size()));
				} else {
					timeCurr = Integer.MAX_VALUE;
					for (int j = 0; j < iNumberOfLandmarks; j++) {	// Traverse the array to find the smallest traveling time from the last landmark.
						boolean isDup = false;						// This variable is used to indicate whether the element already exists in the array.
						for (int h = 0; h < i; h++) {				// Continue to look for elements that have not appeared before.
							if (j == perm[h]) {
								isDup = true;
								break;
							}
						}
						if (isDup) {								// If the current element is repeated, enter the next cycle.
							continue;
						}
						timeCand = oObjectiveFunction.getTravelTime(perm[i-1], j);
						if (timeCand < timeCurr) {					// If the travel time of the current landmark is less than timeCurr, update timeCurr.
							timeCurr = timeCand;
						}
					}
					for (int j = 0; j < iNumberOfLandmarks; j++) {	// Traverse the array to find landmarks whose traveling time is the smallest.
						boolean isDup = false;						// This variable is used to indicate whether the element already exists in the array.
						for (int h = 0; h < i; h++) {				// Continue to look for elements that have not appeared before.
							if (j == perm[h]) {
								isDup = true;
								break;
							}
						}
						if (isDup) {								// If the current element is repeated, enter the next cycle.
							continue;
						}
						if (oObjectiveFunction.getTravelTime(perm[i-1], j) == timeCurr) {
							indices.add(j);
						}
					}
					perm[i] = indices.get(oRandom.nextInt(indices.size()));
				}
				indices.clear();
			}
		}
		
		SolutionRepresentationInterface representation = new SolutionRepresentation(perm);
		double objectiveFunctionValue = oObjectiveFunction.getObjectiveFunctionValue(representation);
		SDSSTPSolution solution = new SDSSTPSolution(representation, objectiveFunctionValue, iNumberOfLandmarks);
		return solution;
	}

	@Override
	public ObjectiveFunctionInterface getSDSSTPObjectiveFunction() {

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLandmarks() {

		return iNumberOfLandmarks;
	}

	@Override
	public SDSSTPLocation getLocationForLandmark(int deliveryId) {

		return aoLandmarks[deliveryId];
	}

	@Override
	public SDSSTPLocation getTourOffice() {

		return this.oTourOffice;
	}

	@Override
	public ArrayList<SDSSTPLocation> getSolutionAsListOfLocations(SDSSTPSolutionInterface oSolution) {

		ArrayList<SDSSTPLocation> locs = new ArrayList<>();
		locs.add(oTourOffice);
		int[] aiDeliveries = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		for (int i = 0; i < aiDeliveries.length; i++) {
			locs.add(getLocationForLandmark(aiDeliveries[i]));
		}
		locs.add(oTourOffice);
		return locs;
	}

	@Override
	public String getInstanceName() {
		
		return strInstanceName;
	}

}
