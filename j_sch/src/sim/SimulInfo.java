package sim;

import util.S_Log;



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
		S_Log.prn(2, "rel:"+rel);
		S_Log.prn(2, "drop:"+drop);
		S_Log.prn(2, "ms:"+ms);
	}
}
