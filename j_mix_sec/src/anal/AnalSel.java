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
//		return n_preemptive(sort);

		return preemptive(sort);
	}
	
	public static Anal n_preemptive(int sort) {
		if(sort==0) { 
//			return new AnalAMC_np();
			return new AnalAMC_np2();
		} else if(sort==1) {
//			return new AnalAMC_np2();
			return new AnalOPA_np();
		} else if(sort==2) {
			return new AnalRM_np(0);
		} else if(sort==3) {
			return new AnalRM_np(1);
		} 
		return null;
		
	}

	public static Anal preemptive(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD();
//			return new AnalAMC_mix();
		} else if(sort==1) {
			return new AnalAMC();
		} else if(sort==2) {
			return new AnalRM(0);
		} else if(sort==3) {
			return new AnalRM(1);
		} else if(sort==4) {
			return new AnalSMC();
		} 
		return null;
		
	}

}
