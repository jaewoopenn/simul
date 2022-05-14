package anal;


public class AnalSel_ori {

	/*
	 * 0: MC-RUN
	 * 1: MC-ADAPT
	 * 2: EDF-VD
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_IV4();  //HI-MAX
		} else if(sort==1) {
			return new AnalEDF_AD_E();
		} else if(sort==2) {
			return new AnalEDF_VD();
		} else if(sort==3) {
			return new AnalFMC();		
		} 
		return null;
	}


}
