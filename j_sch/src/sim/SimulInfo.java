package sim;

import util.SLog;



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
		SLog.prn(2, "rel:"+rel);
		SLog.prn(2, "drop:"+drop);
		SLog.prn(2, "ms:"+ms);
	}
}
