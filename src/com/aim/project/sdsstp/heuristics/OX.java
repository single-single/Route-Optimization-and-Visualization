package com.aim.project.sdsstp.heuristics;

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
public class OX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public OX(Random random) {
		
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
		int times = 0;												// Get the corresponding number of times according to the input IOM.
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
		int cut1 = 0, cut2 = 0, temp;
		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] child1 = new int[p1.getNumberOfLandmarks()];
		int[] child2 = new int[p2.getNumberOfLandmarks()];
		for (int i = 0; i < times; i++) {
			do {													// Randomly select two non-duplicate points and ensure that they will not always lead to the same offspring.
				cut1 = random.nextInt(p1.getNumberOfLandmarks() + 1);
				cut2 = random.nextInt(p2.getNumberOfLandmarks() + 1);
				if (cut1 > cut2) {									// Let the cut1 less than the cut2.
					temp = cut1;
					cut1 = cut2;
					cut2 = temp;
				}
			} while (cut1 == cut2 || cut2 - cut1 == parent1.length || cut2 - cut1 == parent1.length - 1);
			for (int j = cut1; j < cut2; j++) {						// Copy segments between cut1 and cut2 into offspring.
				child1[j] = parent1[j];
				child2[j] = parent2[j];
			}
			int indexChildren, indexParent1 = cut2, indexParent2 = cut2;
			for (int j = cut2; j < p1.getNumberOfLandmarks() + cut1; j++) {		// Copy the rest from the other parent and omit symbols already present.
				indexChildren = j;
				if (indexChildren > p1.getNumberOfLandmarks() - 1) {			// If the end of children are reached, continue from the beginning.
					indexChildren -= p1.getNumberOfLandmarks();
				}
				if (indexParent1 > p1.getNumberOfLandmarks() - 1) {				// If the end of parent1 is reached, continue from the beginning.
					indexParent1 -= p1.getNumberOfLandmarks();
				}
				if (indexParent2 > p1.getNumberOfLandmarks() - 1) {				// If the end of parent2 is reached, continue from the beginning.
					indexParent2 -= p1.getNumberOfLandmarks();
				}
				for (int h = cut1; h < cut2; h++) {								// Omit symbols in parent2 that are already exists in child1.
					if (parent2[indexParent2] == child1[h]) {
						indexParent2++;
						if (indexParent2 > p1.getNumberOfLandmarks() - 1) {		// If the end of parent2 is reached, continue from the beginning.
							indexParent2 -= p1.getNumberOfLandmarks();
						}
						h = cut1 - 1;
					}
				}
				for (int h = cut1; h < cut2; h++) {								// Omit symbols in parent1 that are already exists in child2.
					if (parent1[indexParent1] == child2[h]) {
						indexParent1++;
						if (indexParent1 > p1.getNumberOfLandmarks() - 1) {		// If the end of parent1 is reached, continue from the beginning.
							indexParent1 -= p1.getNumberOfLandmarks();
						}
						h = cut1 - 1;
					}
				}
				child1[indexChildren] = parent2[indexParent2];					// Copy the symbol in parent2 into child1.
				child2[indexChildren] = parent1[indexParent1];					// Copy the symbol in parent1 into child2.
				indexParent1++;
				indexParent2++;
			}
			System.arraycopy(child1, 0, parent1, 0, child1.length);	// Continue to apply crossover to each successive round of offspring.
			System.arraycopy(child2, 0, parent2, 0, child2.length);
		}
		int[] child;
		if (random.nextBoolean()) {									// Randomly select a child as the final result.
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
