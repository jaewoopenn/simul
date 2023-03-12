package sim.mc;
//..

import java.util.Vector;

import sim.job.Job;
import sim.job.JobMng;


public class JobMngMC extends JobMng {
	public JobMngMC(int n){
		super(n);
	}

	public void modeswitch(int tid) {
		Vector<Job> v=new Vector<Job>();
		for(Job j:g_jobs){
			if(j.tid==tid) {
				j.ms();
				j.setVD(j.dl);
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
	
	public void drop(int tid) {
		while(true) {
			Job t_j=null;
			for(Job j:g_jobs){
				if(j.isDrop())
					continue;
				if(j.tid==tid&&j.exec>0) {
					t_j=j;
					
				}
			}
			if(t_j==null)
				break;
			t_j.drop();
			g_jobs.remove(t_j);
			g_jobs.add(t_j);
		}
		
	}
	public int degrade(int tid) { // for IMC
		int d=0;
		while(true) {
			Job t_j=null;
			for(Job j:g_jobs){
				if(j.isDrop())
					continue;
				if(j.tid==tid&&j.exec>0) {
					t_j=j;
					
				}
			}
			if(t_j==null)
				break;
			t_j.degrade();
			d++;
			if(t_j.exec==0) {
				g_jobs.remove(t_j);
				g_jobs.add(t_j);
			}
		}
		return d;
	}
	
	
	
}
