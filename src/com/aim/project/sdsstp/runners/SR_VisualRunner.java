package com.aim.project.sdsstp.runners;


import com.aim.project.sdsstp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Shiqi XIN
 *
 * Runs the default hyper-heuristic, which is SR_IE_HH.
 */
public class SR_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {
		
		return new SR_IE_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new SR_VisualRunner();
		runner.run();
	}
	
}
