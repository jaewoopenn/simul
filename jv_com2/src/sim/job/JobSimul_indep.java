package sim.job;



public class JobSimul_indep extends JobSimul {
	public JobSimul_indep(int n){
		super(n);
	}
	public void setJM(JobMng jm) {
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
			simul_one();
		}
		return 1;
	}	
}
