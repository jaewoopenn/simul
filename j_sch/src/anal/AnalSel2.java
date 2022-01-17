package anal;


public class AnalSel2 {

	/*
	 * 0: MC-RUN
	 * 1: MC-ADAPT
	 * 2: EDF-VD
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
//			return new AnalEDF_IV();  //HI MAX
//			return new AnalEDF_IV2();  //LO MAX
//			return new AnalEDF_IV3();  //hi-only
			return new AnalEDF_IV4();  //HI MAX
		} else if(sort==1) {
			return new AnalEDF_AD_E();
//			return new AnalEDF_AD_E("MC-FLEX");
		} else if(sort==2) {
			return new AnalEDF_VD();
		} 
		return null;
	}

}