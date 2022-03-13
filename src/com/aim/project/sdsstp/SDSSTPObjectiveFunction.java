package com.aim.project.sdsstp;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

public class SDSSTPObjectiveFunction implements ObjectiveFunctionInterface {

	private final int[][] aiTimeDistanceMatrix;

	private final int[] aiTimeDistancesFromTourOffice;

	private final int[] aiTimeDistancesToTourOffice;

	private final int[] aiVisitingDurations;

	public SDSSTPObjectiveFunction(int[][] aiTimeDistanceMatrix, int[] aiTimeDistancesFromTourOffice,
			int[] aiTimeDistancesToTourOffice, int[] aiVisitingDurations) {

		this.aiTimeDistanceMatrix = aiTimeDistanceMatrix;
		this.aiTimeDistancesFromTourOffice = aiTimeDistancesFromTourOffice;
		this.aiTimeDistancesToTourOffice = aiTimeDistancesToTourOffice;
		this.aiVisitingDurations = aiVisitingDurations;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solution) {

		// TODO
		int value = 0;
		int[] perm = solution.getSolutionRepresentation();
		for (int i = 0; i < perm.length; i++) {
			if (i == 0) {
				value += getTravelTimeFromTourOfficeToLandmark(perm[i]);
				value += getVisitingTimeAt(perm[i]);
			} else if (i == perm.length - 1) {
				value += getTravelTime(perm[i-1], perm[i]);
				value += getTravelTimeFromLandmarkToTourOffice(perm[i]);
				value += getVisitingTimeAt(perm[i]);
			} else {
				value += getTravelTime(perm[i-1], perm[i]);
				value += getVisitingTimeAt(perm[i]);
			}
		}
		return value;
	}

	@Override
	public int getTravelTime(int location_a, int location_b) {

		// TODO
		return aiTimeDistanceMatrix[location_a][location_b];
	}

	@Override
	public int getVisitingTimeAt(int landmarkId) {

		// TODO
		return aiVisitingDurations[landmarkId];
	}

	@Override
	public int getTravelTimeFromTourOfficeToLandmark(int toLandmarkId) {
		
		// TODO
		return aiTimeDistancesFromTourOffice[toLandmarkId];
	}

	@Override
	public int getTravelTimeFromLandmarkToTourOffice(int fromLandmarkId) {
		
		// TODO
		return aiTimeDistancesToTourOffice[fromLandmarkId];
	}
}
