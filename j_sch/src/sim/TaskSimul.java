package sim;


import sim.job.Job;
import sim.job.JobSimul;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.SLog;
import util.MRand;

public class TaskSimul {
	protected String g_name="";
	protected SysMng g_sm;
	protected TaskMng g_tm;
	protected MRand g_rutil=new MRand();
	private JobSimul g_js;
	protected SimulInfo g_si;

	public String getName() {
		return g_name;
	}

	
	public void init_sm_tm(SysMng sm,TaskMng tm ){
		if(sm!=null) {
			tm.setX(sm.getX());
			g_sm=sm;
		}
		else {
			SLog.err("sm null");
		}
		g_tm=tm;
		init();
		check_err();
	}
	

	


	// simul interval
	public void simul(int st, int et){
		int t=st;
		if(t==0){
			SLogF.prn("rel  / exec / t");
		}
		while(t<et){
			simul_one();
			t++;
		}
	}
	
	
	public void simul_end() {
		g_js.simul_end();
	}

	// get param
	public SimulInfo getSI(){
		return g_si;
	}
	public TaskMng getTM(){
		return g_tm;
	}
	
	// ------------- protected, override 
	protected void init() {
		g_js=new JobSimul(g_tm.size());
		g_si=new SimulInfo();
	}

	
	protected void simul_one(){   
		release_jobs();
		g_si.drop+=g_js.simul_one();
		//Log.prn(isSchTab,1, " "+t);
	}
	protected Job rel_one_job(Task tsk, int t) {
		return new Job(tsk.tid,t+tsk.period,tsk.c_l);
	}
	
	
	
	// ------------- protected
	protected void check_err() {
		if(g_tm==null){
			SLog.err("ERROR: TaskMng is not set");
		}
	}	
	protected void release_jobs(){
		int t=g_js.get_time();
		String s="";
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period==0){
				s+="+";
				g_js.add(rel_one_job(tsk,t));
			} else {
				s+="-";
			}
		}
		s+=" ";
		SLogF.prnc(s);
	}
	


	
}
