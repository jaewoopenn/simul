package anal;

import com.PRM;

import task.Task;
import util.MCal;
import util.SLog;

public class AnalEDF extends Anal{
	public AnalEDF() {
		super();
		g_name="CSF";
	}	
	
	@Override
	public boolean is_sch() {
		return checkSch(g_prm);
	}	


	public long getLCM() {
		long lcm=MCal.lcm(g_ts.getPeriods());
		return lcm;
	}
	
	@Override
	public boolean checkSch(PRM p) {
		long end_t=getLCM();
		return checkSch_in(p,end_t);
	}

	public boolean checkSch_in(PRM p,  long end_t) {
		int log_lv=1;
		SLog.prn(log_lv, " end_t:"+end_t);
		SLog.prn(log_lv, "t \t sup \t req ");
		String st="";
		for(int t=1;t<=end_t;t++) {
			double d=g_ts.computeDBF(t);
			double s=p.sbf(t);
			st=t+"\t"+s;
			if (s+g_error<d) {
				st+=" <<<<"+"\t"+d+"\t";
				SLog.prn(log_lv,st );
				return false;
			} else {
				st+=" >"+"\t"+d+"\t";
			}
			SLog.prn(log_lv,st );
		}
		
		return true;
	}

	@Override
	public double getExec(int p) {
		long end_t=getLCM();
		for(int t=0;t<end_t;t++) {
			double d=getExec(p,t);
			SLog.prn(1,t+" "+d );
			
		}
		return 2;
	}
	
	public double getExec(int pi, int t) {
		double req=g_ts.computeDBF(t);
		return req;
	}
}
	
