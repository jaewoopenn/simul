package sim.job;


import util.Log;

public class JobSimulMC extends JobSimul{
	public JobSimulMC(){
		g_jm=new JobMngMC();
	}
	public void setJM(JobMngMC jm) {
		g_jm=jm;
	}
	public boolean progress(int cur_t, boolean b){
		Job j=g_jm.getCur();
		int out_type=0;
		if(j==null)	{
			g_jm.prnJob(true,null,out_type);
			return false;
		}
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			if (!j.isHI||j.add_exec==0)
				g_jm.removeCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		g_jm.prnJob(b,j,out_type);
		return true;
	}	
	public int msCheck(int cur_t) { // before dlcheck
		Job j;
		while(true){
			j=(Job)g_jm.getCur();
			if(j==null)
				return -1;
			if(j.exec==0){
				if(j.isHI==false){
					g_jm.removeCur();
				} else{
					if(cur_t>j.vd){
						Log.prn(9, "Job_simul: vd miss"+j.tid);
						Log.prn(9, cur_t+" "+j.vd+" "+j.dl);
						System.exit(1);
					}
					return j.tid;
				}
				
			}
			else
				break;
		}
		return -1;
	}
	
	public JobMngMC getJM() {
		return (JobMngMC) g_jm;
	}
	
	
}
