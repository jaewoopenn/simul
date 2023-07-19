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
//			return new AnalAMC_np();
			return new AnalAMC_np2();
		} else if(sort==1) {
//			return new AnalAMC_np2();
			return new AnalRM_np(0);
		} else if(sort==2) {
			return new AnalRM_np(1);
		} 
		return null;
//
//		if(sort==0) { 
//			return new AnalAMC_np();
//		} else if(sort==1) {
//			return new AnalSMC_np();
//		} else if(sort==2) {
//			return new AnalRM_np(0);
//		} else if(sort==3) {
//			return new AnalRM_np(1);
//		} 
//		return null;
		
		
		
//		if(sort==0) { 
//			return new AnalAMC();
//		} else if(sort==1) {
//			return new AnalSMC();
//		} else if(sort==2) {
//			return new AnalRM();
//		} else if(sort==3) {
//			return new AnalCM();
//		} 
//		return null;

	}


}
