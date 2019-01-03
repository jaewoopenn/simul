package sim;


import basic.Task;
import basic.TaskMng;
import sim.job.Job;
import sim.job.JobSimul;
import util.Log;
import util.RUtil;

public class TaskSimul {
	protected SimulInfo g_si;
	protected SysMng g_sm;
	protected TaskMng g_tm;
	protected RUtil g_rutil=new RUtil();
	protected boolean isSchTab=true;
	private JobSimul g_js;

	public void setSchView(boolean b) {
		isSchTab=b;
	}
	
	public void init_sm_tm(SysMng sm,TaskMng tm ){
		if(sm!=null) {
			tm.setX(sm.getX());
			g_sm=sm;
		}
		g_tm=tm;
		init();
		checkErr();
	}
	

	


	// simul interval
	public void simulBy(int st, int et){
		if(st==0){
			Log.prn(isSchTab,1, "rel  / exec / t");
		}
		int t=st;
		while(t<et){
			simul_t();
			t++;
		}
	}
	
	
	public void simulEnd() {
		g_js.simulEnd();
	}

	// get param
	public SimulInfo getSI(){
		return g_si;
	}
	public TaskMng getTM(){
		return g_tm;
	}
	public void prnInfo() {
		
	}
	
	// ------------- protected
	protected void init() {
		g_js=new JobSimul(g_tm.size());
		g_si=new SimulInfo();
	}

	protected void simul_t(){
		relCheck();
		g_js.simul_one();
		//Log.prn(isSchTab,1, " "+t);
	}
	
	protected Job relJob_base(Task tsk, int t) {
		return new Job(tsk.tid,t+tsk.period,tsk.c_l);
	}
	protected void checkErr() {
		if(g_tm==null){
			Log.prn(9, "ERROR: TaskMng is not set");
			System.exit(1);
		}
	}	
	
	
	// -------------- private
	
	
	private void relCheck(){
		int t=g_js.getTime();
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period==0){
				Log.prnc(isSchTab,1,"+");
				g_js.add(relJob_base(tsk,t));
			} else {
				Log.prnc(isSchTab,1,"-");
			}
		}
		Log.prnc(isSchTab,1, " ");
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}
	

	
}
