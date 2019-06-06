package anal;

import com.PRM;

import task.TaskSet;
import util.MList;
//import util.SLog;

public class AnalDraw {
	private TaskSet g_ts;
	private PRM g_prm;
	private MList g_ml;
	public AnalDraw(TaskSet ts,PRM p) {
		g_ts=ts;
		g_prm=p;
		g_ml=new MList();
	}
	public void make(String fn,int i, int end_t) {
		double t=0;
		double next_t=0;
//		int i=0;
		double d=0;
		double old_d=0;
		for(t=0;t<end_t;t++) {
			next_t=g_prm.nextPt(t);
			d=g_ts.computeRBF(i, t);
			if(d != old_d) {
				prn2(t,i);
				old_d=d;
			}
			if(next_t<=t+1) {
				prn(next_t,i);
			}
		}
		g_ml.save(fn);
		
	}
	public void prn2(double t,int i) {
		prn(t,i);
		prn(t+0.001,i);
	}
	public void prn(double t,int i) {
		if(t<0.001)
			return;
		String st="";
		st+=t;
		st+=" "+g_prm.sbf_d(t);
		st+=" "+g_ts.computeRBF(i, t-0.001);
		g_ml.add(st);
	}
}
