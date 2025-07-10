package sim;


import sim.job.Job;
import task.DTaskVec;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.MRand;

public abstract class TaskSimul_base {
	protected String g_name="";
	protected SysMng g_sm;
	protected DTaskVec g_dt;
	protected TaskMng g_tm;
	protected MRand g_rutil=new MRand();
	protected SimulInfo g_si;

	public String getName() {
		return g_name;
	}

	
	public abstract void init_sm_dt(SysMng sm, DTaskVec tm );
	

	

 
	// simul interval
	public void simul(int et){
		int t=0;
		g_si.total=et;
		SLogF.prn("rel  / exec / t");
		while(t<et){
			simul_one();
			t++;
		}
		simul_end();
	}
	
	
	protected abstract void simul_end() ;

	// get param
	public SimulInfo getSI(){
		return g_si;
	}
	public void prnSI() {
		g_si.prn2();
	}
	public TaskMng getTM(){
		return g_tm;
	}
	
	// ------------- protected, override 
	protected abstract void init() ;
	
	protected abstract void simul_one();
	
	protected abstract Job rel_one_job(Task tsk, int t) ;
	
	
	protected abstract void release_jobs();


	
}
