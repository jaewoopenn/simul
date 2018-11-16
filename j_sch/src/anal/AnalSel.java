package anal;


public class AnalSel {
	public static Anal getAnal(int sort) {
		if(sort==0) {
			return new AnalEDF_VD();
		} else if(sort==1) {
			return new AnalEDF();
		}
		return null;
	}
}
