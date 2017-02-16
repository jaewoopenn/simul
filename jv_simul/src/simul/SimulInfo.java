package simul;

import util.Log;



public class SimulInfo {
	public int rel;
	public int drop;
	public int ms;
	public double getDMR(){
		if(rel==0)
			return 0;
		else
			return (double)drop/rel;
	}
	public void prn() {
		Log.prn(2, "rel:"+rel);
		Log.prn(2, "drop:"+drop);
		Log.prn(2, "ms:"+ms);
	}
}
