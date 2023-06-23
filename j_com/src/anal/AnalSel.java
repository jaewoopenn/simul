package anal;


public class AnalSel {
//	public static int a=0;  //rm
	public static int a=1;  //edf

	public static Anal getAnal(int sort) {
		if(a==0)
			return getAnal_rm(sort);
		else
			return getAnal_edf(sort);
			
	}

	public static Anal getAnal_rm(int sort) {
		if(sort==0) { 
			return new AnalRM();
		} else if(sort==1) {
			return new AnalRM_int();
		} else if(sort==2) {
			return new AnalRM_iplus();
		} else if(sort==3) {
			return new AnalRM_dprm();
		}
		return null;
	}
	
	public static Anal getAnal_edf(int sort) {
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
