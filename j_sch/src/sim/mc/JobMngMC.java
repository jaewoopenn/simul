package sim.mc;

import java.util.Vector;

import sim.job.Job;
import sim.job.JobMng;
import util.S_Log;


public class JobMngMC extends JobMng {
	public JobMngMC(int n){
		super(n);
	}

	public void modeswitch(int tid) {
		Vector<Job> v=new Vector<Job>();
		for(Job j:g_jobs){
			if(j.tid==tid) {
				j.exec=j.exec+j.add_exec;
				((Job)j).vd=j.dl;
				j.add_exec=0;
				v.add(j);
			}
		}
		
		// because java.util.ConcurrentModificationException
		for(Job j:v){
			g_jobs.remove(j);
		}
		for(Job j:v){
			g_jobs.add(j);
		}
		
	}
	
	public int drop(int tid) {
		int d_num=0;
		for(Job j:g_jobs){
			if(j.tid==tid&&j.exec>0) {
				j.exec=0;
				d_num++;
			}
		}
//		S_Log.prn(1, d_num);
		if(d_num>1){
			S_Log.err("JobMngMC: drop num>1");
		}
		return d_num;
		
	}

}
