package com.aim.project.sdsstp.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation)
 * - public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2, SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation)
 * - public boolean isCrossover()
 * - public boolean usesIntensityOfMutation()
 * - public boolean usesDepthOfSearch()
 */
public class CX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public CX(Random random) {
		
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO 
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2,
			SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		// TODO 
		int times = 0;										// Get the corresponding number of times according to the input IOM.
		if (0.0 <= intensityOfMutation && intensityOfMutation < 0.2) {
			times = 1;
		} else if (0.2 <= intensityOfMutation && intensityOfMutation < 0.4) {
			times = 2;
		} else if (0.4 <= intensityOfMutation && intensityOfMutation < 0.6) {
			times = 3;
		} else if (0.6 <= intensityOfMutation && intensityOfMutation < 0.8) {
			times = 4;
		} else if (0.8 <= intensityOfMutation && intensityOfMutation < 1.0) {
			times = 5;
		} else {
			times = 6;
		}
		int startPoint = 0, currentPoint = 0;
		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] child1 = new int[p1.getNumberOfLandmarks()];
		int[] child2 = new int[p2.getNumberOfLandmarks()];
		for (int i = 0; i < times; i++) {
			Arrays.fill(child1, -1);						// Set all elements in children to -1, this is to identify the elements that have not been assigned.
			Arrays.fill(child2, -1);
			startPoint = random.nextInt(p1.getNumberOfLandmarks());		// Randomly select a point in parent1 as the starting point.
			currentPoint = startPoint;
			do {											// Copy cities from p1 to c1 and p2 to c2 until a cycle is obtained while mapping from p1 to each corresponding city in p2.
				for (int j = 0; j < parent1.length; j++) {
					if (parent1[j] == parent2[currentPoint]) {
						child1[j] = parent1[j];
						child2[j] = parent2[j];
						currentPoint = j;
						break;
					}
				}
			} while (startPoint != currentPoint);
			for (int j = 0; j < child1.length; j++) {		// Fill rest of c1 from p2 and c2 from p1, these elements that were not assigned in the previous stage are indicated by -1.
				if (child1[j] == -1) {
					child1[j] = parent2[j];
					child2[j] = parent1[j];
				}
			}
			System.arraycopy(child1, 0, parent1, 0, child1.length);		// Continue to apply crossover to each successive round of offspring.
			System.arraycopy(child2, 0, parent2, 0, child2.length);
		}
		int[] child;
		if (random.nextBoolean()) {							// Randomly select a child as the final result.
			child = child1;
		} else {
			child = child2;
		}
		c.getSolutionRepresentation().setSolutionRepresentation(child);
		c.setObjectiveFunctionValue(f.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {

		// TODO 
		return true;
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

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
}
