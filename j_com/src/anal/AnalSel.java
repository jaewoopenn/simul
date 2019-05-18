package anal;


public class AnalSel {
	/*
	 * 0: RM
	 * 1: RM
	 */
	
	public static Anal getAnal(int sort) {
		if(sort==0) { 
			return new AnalRM();
		} else if(sort==1) {
			return new AnalRM();
		}
		return null;
	}
}
