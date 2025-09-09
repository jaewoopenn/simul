package sim;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import anal.AnalEDF_VD_IMC;

public class SimulSel_IMC {
	/*
	 * 0: EDF-IV
	 * 1: EDF-AD
	 * 2: EDF-VD
	 */
	
	public static TaskSimul getSim(int sort) {
		if(sort==0) {
			return new TaskSimul_EDF_VD_ADM();
		} else if(sort==1) {
			return new TaskSimul_EDF_VD_IMC();
		} else if(sort==2) {
			return null;
		}
		return null;
	}
	
	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_ADM();
		} else if(sort==1) {
			return new AnalEDF_VD_IMC();
		} else if(sort==2) {
			return null;
		} 
		return null;
	}
	
}
