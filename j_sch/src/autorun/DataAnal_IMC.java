package autorun;

import java.util.Vector;

import anal.Anal;
import anal.AnalSel_ori;
import anal_imc.AnalSel_IMC;
import auto.DataAnal;
import util.MList;

public class DataAnal_IMC extends DataAnal {

	public DataAnal_IMC(String path, int x) {
		super(path, x);
	}


	public Anal getAnal(int i) {
		return AnalSel_IMC.getAnal(i);
	}	
	
}
