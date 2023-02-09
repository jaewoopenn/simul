package sim;

import util.SLog;



public class SimulInfo {
	public int rel;
	public int nrel;
	public int drop;
	public int ms;
	public double getDMR(){
		if(rel==0)
			return 0;
		else
			return (double)(nrel+drop)/rel;
	}
	public void prn() {
		SLog.prn(2, "rel:"+rel);
		SLog.prn(2, "no rel:"+nrel);
		SLog.prn(2, "drop:"+drop);
		SLog.prn(2, "nr+drop:"+(nrel+drop));
		SLog.prn(2, "ms:"+ms);
		SLog.prn(2, "dmr:"+getDMR());
	}
}
