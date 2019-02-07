package sim.mc;


import sim.job.Job;
import sim.job.JobSimul;
import util.FLog;
import util.Log;

public class JobSimulMC extends JobSimul{
	public JobSimulMC(int n){
		super(0);
		g_jm=new JobMngMC(n);
	}
	
	
	//@override
	public JobMngMC getJM() {
		return (JobMngMC) g_jm;
	}
		
	//@override
	protected void exec_one(){
		Job j=g_jm.getCur();
		int out_type=0;
		String s="";
		if(j==null)	{
			
		}
		else if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			// HI task and add_exec>0 --> process in ms_check
			if(!j.isHI||j.add_exec==0) {
				g_jm.removeCur();
			}

		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		s=g_jm.getJobArrow(j,out_type);
		FLog.prn(s);
	}	
	
	// before dl_check
	public int ms_check() { 
		Job j;
		while(true){
			j=(Job)g_jm.getCur();
			if(j==null)
				return -1;
			if(j.isHI){
				if(g_t>j.vd){
					Log.prn(9, "Job_simul: vd miss"+j.tid);
					Log.prn(9, g_t+" vd:"+j.vd+" dl:"+j.dl+" exec:"+j.exec);
					System.exit(1);
				}
				if(j.exec==0 && j.add_exec>0) 
					return j.tid;
				else
					break;
				
			}
			else
				break;
		}
		return -1;
	}
	

	
}
