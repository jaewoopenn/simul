package anal;


public class AnalSel {
	
//	public static Anal getAnal(int sort) {
//		if(sort==0) { 
//			return new AnalRM();
//		} else if(sort==1) {
//			return new AnalRM_int();
//		} else if(sort==2) {
//			return new AnalRM_iplus();
//		} else if(sort==3) {
//			return new AnalRM_dprm();
//		}
//		return null;
//	}
	
	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalEDF();
		} else if(sort==1) {
			return new AnalEDF_int();
		} else if(sort==2) {
			return new AnalEDF_iplus();
		} else if(sort==3) {
			return new AnalRM();
		}
		return null;
	}	
}
