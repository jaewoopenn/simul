package simul;

import utill.Log;



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
		Log.prn(1, "rel:"+rel);
		Log.prn(1, "drop:"+drop);
		Log.prn(1, "ms:"+ms);
	}
}
