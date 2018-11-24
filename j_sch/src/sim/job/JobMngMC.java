package sim.job;

import java.util.Vector;


public class JobMngMC extends JobMng {
	public void modeswitch() {
		for(Job j:g_jobs){
			j.exec=j.exec+j.add_exec;
			j.add_exec=0;
		}
		
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
//		Log.prn(1, d_num);
		return d_num;
		
	}

}
