package com.aim.project.sdsstp.runners;


import com.aim.project.sdsstp.hyperheuristics.RL_SA_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Shiqi XIN
 *
 * Runs my own hyper-heuristic, which is RL_SA_HH.
 */
public class RL_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {
		
		return new RL_SA_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new RL_VisualRunner();
		runner.run();
	}
	
}
