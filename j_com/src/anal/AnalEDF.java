package anal;

import com.PRM;

import task.Task;
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


	public int getLCM() {
		int lcm=0;
		for(Task t:g_ts.getArr()) {
			lcm+=t.period;
		}
		return lcm;
	}
	public boolean checkSch(PRM p) {
		int end_t=getLCM();
		return checkSch_in(p,end_t);
	}

	public boolean checkSch_in(PRM p,  int end_t) {
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
		// TODO Auto-generated method stub
		return 0;
	}
	

}
	
