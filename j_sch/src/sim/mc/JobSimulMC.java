package sim.mc;


import sim.job.Job;
import sim.job.JobSimul;
import util.SLogF;

public class JobSimulMC extends JobSimul{
	public JobSimulMC(int n){
		super(0);
		g_jm=new JobMngMC(n);
	}
	
	
	@Override
	public JobMngMC getJM() {
		return (JobMngMC) g_jm;
	}
		
	@Override
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
		SLogF.prn(s);
	}	
	
	// do ms check after exec
	public Job get_ms_job() { 
		Job j=(Job)g_jm.getCur();
		if(j==null)
			return null;
		if(j.exec==0) {
			
			if(!j.isHI) {
				g_jm.removeCur();
				return null;
			} else { //HI
				return j;
			}
		}
		return null;
	}
	

	
}
