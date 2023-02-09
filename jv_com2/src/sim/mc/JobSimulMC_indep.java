package sim.mc;

import sim.job.Job;

/*
 * only for test
 */


public class JobSimulMC_indep extends JobSimulMC {
	public JobSimulMC_indep(int n){
		super(n);
	}
	
	public void setJM(JobMngMC jm) {
		g_jm=jm;
	}
	
	// all in one
	public int simul(int et){
		simulBy(et);
		simul_end();
		return g_jm.endCheck(et);
	}
	
	public int simulBy(int et){
		while(g_t<et){
			Job j=get_ms_job();
			if(j!=null) {
				this.getJM().modeswitch(j.tid);
			}
			simul_one();
		}
		return 1;
	}	
}
