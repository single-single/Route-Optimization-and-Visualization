package com.aim.project.sdsstp.solution;

import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

public class SDSSTPSolution implements SDSSTPSolutionInterface {

	private SolutionRepresentationInterface representation;
	
	private double objectiveFunctionValue;
	
	private int numberOfVariables;
	
	public SDSSTPSolution(SolutionRepresentationInterface representation, double objectiveFunctionValue, int numberOfVariables) {
		
		this.representation = representation;
		this.objectiveFunctionValue = objectiveFunctionValue;
		this.numberOfVariables = numberOfVariables;
	}

	@Override
	public double getObjectiveFunctionValue() {

		// TODO
		return objectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(double objectiveFunctionValue) {
		
		// TODO
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		
		// TODO
		return representation;
	}
	
	@Override
	public SDSSTPSolutionInterface clone() {
		
		// TODO
		SolutionRepresentationInterface rep = representation.clone();
		SDSSTPSolutionInterface solution = new SDSSTPSolution(rep, objectiveFunctionValue, numberOfVariables);
		return solution;
	}

	@Override
	public int getNumberOfLandmarks() {
		
		// TODO
		return numberOfVariables;
	}
}
