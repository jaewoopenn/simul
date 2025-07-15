package anal;

public class AnalSel_MC {

	/*
	 * 0: EDF-VD-ADM
	 * 1: EDF-VD
	 * 2: EDF
	 * 3: EDF-BV
	 */

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_ADM();
		} else if(sort==1) {
			return new AnalEDF_BV();
		} else if(sort==2) {
			return new AnalEDF_VD_IMC();
		} else if(sort==3) {
			return new AnalEDF_IMC();
		} 
		return null;
	}

	
}
