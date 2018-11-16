package sim;

import util.Log;



public class SimulInfo {
	public int rel;
	public int drop;
	public int ms;
	public int mig;
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
		Log.prn(2, "mig:"+mig);
	}
}
