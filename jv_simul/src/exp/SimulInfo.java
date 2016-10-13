package exp;

import utilSim.Log;



public class SimulInfo {
	public int rel;
	public int drop;
	public int ms;
	public double getDMR(){
		return (double)drop/rel;
	}
	public void prn() {
		Log.prn(1, "rel:"+rel);
		Log.prn(1, "drop:"+drop);
		Log.prn(1, "ms:"+ms);
	}
}
