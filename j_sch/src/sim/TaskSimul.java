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
	protected JobSimul g_js;
	protected RUtil g_rutil=new RUtil();
	public boolean isSchTab=true;

	protected void init() {
		g_js=new JobSimul();
		g_si=new SimulInfo();
	}
	public void setSchView(boolean b) {
		isSchTab=b;
	}
	
	public void init_sm(SysMng sm){
		g_tm.setX(sm.getX());
		g_sm=sm;
		
	}
	
	public void init_tm(TaskMng tm) {
		g_tm=tm;
		init();
		
	}
	public void checkErr() {
		if(g_tm==null){
			Log.prn(9, "ERROR: TaskMng is not set");
			System.exit(1);
		}
		
	}
	

	public void simul(int st,int et){
		simulBy(st,et);
		simulEnd();
	}

	// simul interval
	public void simulBy(int st, int et){
		if(st==0){
			simulStart();
		}
		int t=st;
		while(t<et){
			simul_t();
			t++;
		}
	}
	
	// simul time
	public void simulStart()
	{
		Log.prn(isSchTab,1, "rel  / exec / t");
	}
	
	public void simul_t(){
		relCheck();
		g_js.simul_one();
		//Log.prn(isSchTab,1, " "+t);
	}
	public void simulEnd() {
		g_js.simulEnd();
	}
	
	


	
	private void relCheck(){
		int t=g_js.getTime();
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				Log.prnc(isSchTab,1,"-");
				continue;
			}
			Log.prnc(isSchTab,1,"+");
			g_js.add(relJob_base(tsk,t));
		}
		Log.prnc(isSchTab,1, " ");
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
	

	
	protected Job relJob_base(Task tsk, int t) {
		return new Job(tsk.tid,t+tsk.period,tsk.c_l);
	}

	
}
