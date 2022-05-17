package imc;

import anal.Anal;

public class AnalSel_IMC {

	/*
	 * 0: MC-RUN
	 * 1: MC-ADAPT
	 * 2: EDF-VD
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_RUN();  //HI-MAX
		} else if(sort==1) {
			return new AnalEDF_VD_IMC();
		} else if(sort==2) {
			return new AnalEDF_IMC();
		} 
		return null;
	}

	public static Anal getAnalSim(int sort) {
		if(sort==0) { 
			return new AnalEDF_RUN();  //HI-MAX
		} else if(sort==1) {
			return new AnalEDF_VD_IMC();
		} else if(sort==2) {
			return new AnalEDF_IMC();
		} 
		return null;
	}
	
}
