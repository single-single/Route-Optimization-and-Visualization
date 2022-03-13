package com.aim.project.sdsstp.solution;

import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] representation;
	
	public SolutionRepresentation(int[] representation) {
		
		this.representation = representation;
	}
	
	@Override
	public int[] getSolutionRepresentation() {

		// TODO
		return representation;
	}

	@Override
	public void setSolutionRepresentation(int[] solution) {
		
		// TODO
		representation = solution;
	}

	@Override
	public int getNumberOfLandmarks() {

		// TODO
		return representation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		
		// TODO
		int[] perm = representation.clone();
		SolutionRepresentationInterface rep = new SolutionRepresentation(perm);
		return rep;
	}

}
