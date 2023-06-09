package anal;

import com.PRM;

import task.TaskSet;
import util.MList;
//import util.SLog;

public class AnalDraw {
	private MList g_ml;
	private TaskSet g_ts;
	private PRM g_prm;
	public AnalDraw() {
		g_ml=new MList();
	}
	public void save(String fn) {
		g_ml.save(fn);
		
	}
	
	public void draw_rbf(TaskSet ts,int i, int end_t) {
		g_ts=ts;
		double t=0;
		double d=0;
		double old_d=0;
		for(t=0;t<end_t;t++) {
			d=g_ts.computeRBF(i, t);
			if(d != old_d) {
				prn2(t,i);
				old_d=d;
			}
		}
		g_ml.add("---");
		
	}
	public void draw_dbf(TaskSet ts, int end_t) {
		g_ts=ts;
		double t=0;
		double d=0;
		double old_d=0;
		prn(0.002);
		for(t=0;t<end_t;t++) {
			d=g_ts.computeDBF(t);
			if(d != old_d) {
				prn2(t);
				old_d=d;
			}
		}
		prn(end_t);
		g_ml.add("---");
		
	}
	
	public void draw_sbf(PRM prm, int end_t) {
		g_prm=prm;
		double t=0;
		double next_t=0;
		for(t=0;t<end_t;t++) {
			next_t=g_prm.nextPt(t);
			if(next_t<=t+1) {
				prn3(next_t);
			}
		}
		g_ml.add("---");
		
	}
	public void prn2(double t) {
		prn(t);
		prn(t+0.001);
	}	
	public void prn(double t) {
		if(t<0.001)
			return;
		String st="";
		st+=t;
		st+=" "+g_ts.computeDBF(t-0.001);
		g_ml.add(st);
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
		st+=" "+g_ts.computeRBF(i, t-0.001);
		g_ml.add(st);
	}
	public void prn3(double t) {
		String st="";
		st+=t;
		st+=" "+g_prm.sbf_d(t);
		g_ml.add(st);
		
	}
}
