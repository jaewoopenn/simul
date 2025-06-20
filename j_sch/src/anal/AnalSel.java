package anal;


public class AnalSel {
	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF();
		} else if(sort==1) {
			return new AnalEDF_VD();
		} else if(sort==2) {
			return new AnalEDF_BV();
		}
		return null;
	}

	/*
	 * 0: EDF-IV
	 * 1: EDF-AD
	 * 2: EDF-VD
	 * 3: EDF
	 * 4: ICG
	 */

//	public static Anal getAnal(int sort) {
//		if(sort==0) { 
//			return new AnalEDF_IV2();
//		} else if(sort==1) {
//			return new AnalEDF_AD_E("MC-FLEX");
//		} else if(sort==2) {
//			return new AnalEDF_AD_E();
//		} else if(sort==3) {
//			return new AnalEDF_VD();
//		} else if(sort==4) {
//			return new AnalEDF();
//		} else if(sort==5) {
//			return new AnalICG();
//		}
//		return null;
//	}
//
	/*
	 * 0: EDF-AD-p
	 * 1: EDF-AD
	 * 2: EDF-VD
	 * 3: EDF
	 * 4: ICG
	 */
	
////	 original 
//	public static Anal getAnal(int sort) {
//		if(sort==0) { 
//			return new AnalEDF_AD_E("MC-FLEX");
//		} else if(sort==1) {
//			return new AnalEDF_AD_E();
//		} else if(sort==2) {
//			return new AnalFMC();
//		} else if(sort==3) {
//			return new AnalEDF_VD();
//		} else if(sort==4) {
//			return new AnalEDF();
//		} else if(sort==5) {
//			return new AnalICG();
//		}
//		return null;
//	}
	
//   temp 
//	public static Anal getAnal(int sort) {
//		if(sort==0) { 
//			return new AnalEDF_AD_E("MC-FLEX");
//		} else if(sort==1) {
//			return new AnalEDF_AD_E();
//		} else if(sort==2) {
//			return new AnalEDF_VD();
//		} else if(sort==3) {
//			return new AnalFMC();
//		} else if(sort==4) {
//			return new AnalEDF();
//		} else if(sort==5) {
//			return new AnalICG();
//		}
//		return null;
//	}

}
