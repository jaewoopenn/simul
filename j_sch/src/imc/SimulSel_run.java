package imc;

import sim.mc.*;

public class SimulSel_run {
	/*
	 * 0: EDF-IV
	 * 1: EDF-AD
	 * 2: EDF-VD
	 */
	
	public static TaskSimulMC getSim(int sort) {
		if(sort==0) {
			return new TaskSimul_MC_RUN2();
		} else if(sort==1) {
			return new TaskSimul_EDF_AD_E();
		} else if(sort==2) {
			return new TaskSimul_EDF_VD();
		}
		return null;
	}
}
