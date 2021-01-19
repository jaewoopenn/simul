package sim;

import sim.mc.*;

public class SimulSel {
	/*
	 * 0: EDF-AD-p2
	 * 1: EDF-AD-p
	 * 2: EDF-AD
	 * 3: EDF-DQ
	 * 4: EDF-VD
	 */
	
	public static TaskSimulMC getSim(int sort) {
		if(sort==0) {
			return new TaskSimul_EDF_Post2();
		} else if(sort==1) {
			return new TaskSimul_EDF_Post();
		} else if(sort==2) {
			return new TaskSimul_EDF_AD_E();
		} else if(sort==3) {
			return new TaskSimul_FMC();
		} else if(sort==4) {
			return new TaskSimul_EDF_VD();
		}
		return null;
	}
}
