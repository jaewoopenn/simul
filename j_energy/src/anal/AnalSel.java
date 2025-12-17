package anal;


public class AnalSel {


	public static Anal get(int sort) {
		if(sort==0) { 
			return new AnalEDF_VD_IMC();
		} else if(sort==1) {
			return new AnalEDF_IMC();
		} else if(sort==2) {
			return new AnalAMC_imc();
		} else if(sort==99) {
			return new AnalUtil();
		} 
		return null;
	}

	
	
}
