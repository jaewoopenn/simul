package sim;

import util.MCal;
import util.SLog;



public class SimulInfo {
	public int rel;
	public int nrel;
	public int drop;
	public int ms;
	public int delayed;
	public int degraded;
	public int total;
	public int start_delay;
	public int add_task=0;
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
	public void prn2() {
		SLog.prn(2, "ms:"+ms);
		SLog.prn(2, "degraded:"+degraded);
		SLog.prn(2, "delayed:"+delayed);
		SLog.prn(2, "total:"+total);
		double per=(double)degraded/total;
		SLog.prn(2, "degraded pecentage:"+MCal.getStr(per));
		double avg=(double)delayed/add_task;
		SLog.prn(2, "average delay:"+MCal.getStr(avg));
		
	}
	
}
