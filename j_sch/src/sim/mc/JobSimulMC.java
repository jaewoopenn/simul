package sim.mc;


import sim.job.Job;
import sim.job.JobSimul;
import util.S_FLog;

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
			// removing is processing in ms_check 
			// HI task and add_exec>0 --> process in ms_check
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		s=g_jm.getJobArrow(j,out_type);
		S_FLog.prn(s);
	}	
	
	// after exec
	public int ms_check() { 
		Job j=(Job)g_jm.getCur();
		if(j==null)
			return -1;
		if(j.exec==0) {
			if(!j.isHI||j.add_exec==0) {
				g_jm.removeCur();
			}
			if(j.add_exec>0)
				return j.tid;
		}
		return -1;
	}
	

	
}
