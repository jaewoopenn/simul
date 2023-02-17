package autorun;


import anal.Anal;
import auto.DataAnal;
import imc.AnalSel_IMC;

public class DataAnal_IMC extends DataAnal {

	public DataAnal_IMC(String path, int x) {
		super(path, x);
	}


	public Anal getAnal(int i) {
		return AnalSel_IMC.getAnal(i);
	}	
	
}
