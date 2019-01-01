package anal;


public class AnalSel {
	public static Anal getAnal(int sort) {
		if(sort==0) {
			return new AnalEDF_AD_E();
		} else if(sort==1) {
			return new AnalEDF_VD();
		} else if(sort==2) {
			return new AnalICG();
		} else if(sort==3) {
			return new AnalEDF();
		}
		return null;
	}
}
