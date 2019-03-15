package sim;

import sim.mc.TaskSimul_EDF_AD_E;
import sim.mc.TaskSimul_EDF_Post;
import sim.mc.TaskSimul_EDF_VD;

public class SimulSel {
	/*
	 * 0: EDF-AD-p
	 * 1: EDF-AD
	 * 2: EDF-VD
	 */
	
	public static TaskSimul getSim(int sort) {
		if(sort==0) {
			return new TaskSimul_EDF_Post();
		} else if(sort==1) {
			return new TaskSimul_EDF_AD_E();
		} else if(sort==2) {
			return new TaskSimul_EDF_VD();
		} else if(sort==3) {
			return new TaskSimul();
		}
		return null;
	}
}
