package sim;


import sim.job.Job;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.MRand;

public abstract class TaskSimul_base {
	protected String g_name="";
	protected SysMng g_sm;
	protected TaskMng g_tm;
	protected MRand g_rutil=new MRand();
	protected SimulInfo g_si;
	protected  boolean g_recover_idle_on=true;

	public String getName() {
		return g_name;
	}

	
	public abstract void init_sm_tm(SysMng sm,TaskMng tm );
	

	
	public abstract void setBE() ;
	public void setRecoverIdle(boolean b) {
		g_recover_idle_on=b;
	}


	// simul interval
	public void simul(int et){
		int t=0;
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
	public TaskMng getTM(){
		return g_tm;
	}
	
	// ------------- protected, override 
	protected abstract void init() ;
	
	protected abstract void simul_one();
	
	protected abstract Job rel_one_job(Task tsk, int t) ;
	
	
	protected abstract void release_jobs();


	
}
