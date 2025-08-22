package anal;


public class AnalSel {

	/*
	 * 0: MC-RUN
	 * 1: MC-ADAPT
	 * 2: EDF-VD
	 */
	public static Anal getAnalAuto(int sort, boolean isMC) {
		if(isMC)
			return getAnalMC(sort);
		else
			return getAnal(sort);
	}

	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_ADM();
		} else if(sort==1) {
			return new AnalEDF_VD_IMC();
		} else if(sort==2) {
			return new AnalEDF_IMC();
		} else if(sort==3) {
			return new AnalAMC_imc();
		} 
		return null;
	}

	public static Anal getAnalSim(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_ADM();
		} else if(sort==1) {
			return new AnalEDF_VD_IMC();
		} else if(sort==2) {
			return null;
		} 
		return null;
	}
	
	/*
	 * 0: EDF-VD-ADM
	 * 1: EDF-VD
	 * 2: EDF
	 * 3: EDF-BV
	 */

	public static Anal getAnalMC(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_ADM();
		} else if(sort==1) {
			return new AnalEDF_ADAPT();
		} else if(sort==2) {
			return new AnalEDF_BV();
		} else if(sort==3) {
			return new AnalEDF_VD_IMC();
		} else if(sort==4) {
			return new AnalEDF_IMC();
		} 
		return null;
	}
	
}
