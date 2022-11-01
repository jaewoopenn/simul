package imc;

import anal.Anal;
import anal.AnalAMC;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import anal.AnalFMC;

public class AnalSel_run {

	/*
	 * 0: MC-RUN
	 * 1: MC-ADAPT
	 * 2: EDF-VD
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_RUN();  //HI-MAX
		} else if(sort==1) {
			return new AnalEDF_AD_E();
		} else if(sort==2) {
			return new AnalEDF_VD();
		} else if(sort==3) {
			return new AnalFMC();		
		} else if(sort==4) {
			return new AnalAMC();		
		} 
		return null;
	}
	public static Anal getAnalSim(int sort) {
		if(sort==0) { 
			return new AnalEDF_RUN();  //HI-MAX
		} else if(sort==1) {
			return new AnalEDF_AD_E();
		} else if(sort==2) {
			return new AnalEDF_VD();
		} 
		return null;
	}


}
