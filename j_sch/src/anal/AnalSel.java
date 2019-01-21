package anal;


public class AnalSel {
	/*
	 * 0: EDF-AD
	 * 1: EDF-VD
	 * 2: EDF
	 * 3: ICG
	 */
	
	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_AD_E();
		} else if(sort==1) {
			return new AnalEDF_VD();
		} else if(sort==2) {
			return new AnalEDF();
		} else if(sort==3) {
			return new AnalICG();
		}
		return null;
	}
}
