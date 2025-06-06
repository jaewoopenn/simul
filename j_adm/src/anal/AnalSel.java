package anal;


public class AnalSel {
	/*
	 * 0: EDF-IV
	 * 1: EDF-AD
	 * 2: EDF-VD
	 * 3: EDF
	 * 4: ICG
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD();
		} else if(sort==1) {
			return new AnalEDF();
		} else if(sort==2) {
			return new AnalEDF();
		}
		return null;
	}

	public static Anal getAnalSim(int sort) {
//		if(sort==0) { 
//			return new AnalEDF_VD();
//		} else if(sort==1) {
//			return new AnalEDF();
//		} else if(sort==2) {
//			return new AnalICG();
//		}
		return null;
	}
	

}
