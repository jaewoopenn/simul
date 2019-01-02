package sim.job;



public class JobSimulEx extends JobSimul {
	public JobSimulEx(int n){
		super(n);
	}
	public void setJM(JobMng jm) {
		g_jm=jm;
	}
	// all in one
	public int simul(int et){
		simulBy(et);
		simulEnd();
		return g_jm.endCheck(et);
	}
	public int simulBy(int et){
		while(g_t<et){
			simul_one();
		}
		return 1;
	}	
}
