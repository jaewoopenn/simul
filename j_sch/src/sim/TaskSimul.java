package sim;


import basic.Task;
import basic.TaskMng;
import sim.job.Job;
import sim.job.JobSimulMC;
import util.Log;
import util.RUtil;

public abstract class TaskSimul {
	protected SimulInfo g_si;
	protected SysMng g_sm;
	protected TaskMng g_tm;
	protected JobSimulMC g_js;
	protected RUtil g_rutil=new RUtil();
	protected boolean g_needRecover=false;
	public boolean g_recoverOn=true;
	public boolean isSchTab=true;
	public boolean isPrnMS=true;
	public boolean isPrnEnd=true;

	public TaskSimul(TaskMng tm){
		g_tm=tm;
		init();
	}
	private void init() {
		g_js=new JobSimulMC();
		g_si=new SimulInfo();
		g_needRecover=false;
	}
	
	public void init_sm(SysMng sm){
		g_sm=sm;
	}
	
	public void init_tm(TaskMng tm) {
		g_tm=tm;
		init();
		
	}
	public void set_tm(TaskMng tm) {
		g_tm=tm;
		
	}
	public void checkErr() {
		if(g_tm==null){
			Log.prn(9, "ERROR: TaskMng is not set");
			System.exit(1);
		}
		
	}
	

	// simul interval
	public void simulBy(int st, int et){
		if(st==0){
			simulStart();
		}
		int t=st;
		while(t<et){
			simul_t(t);
			t++;
		}
	}
	public void simulEnd(int st,int et){
		simulBy(st,et);
		simulEnd(et);
	}
	
	// simul time
	public void simulStart()
	{
		Log.prn(isSchTab,1, "rel  / exec / t");
		initMode_in();
	}
	
	public void simul_t(int t){
		msCheck(t);
		g_js.dlCheck(t);
		relCheck(t);
		if(!g_js.progress(t,isSchTab)&&g_needRecover&&g_recoverOn) {
			recover(t);
		}
		Log.prn(isSchTab,1, " "+t);
	}
	public void simulEnd(int et) {
		g_js.simulEndPrn();
		g_js.simulEnd(et);
	}
	private void recover(int t){
//		Log.prn(isPrnMS,1, "recover "+t);
		g_needRecover=false;
		recover_in();
		initMode_in();
//		System.exit(0);
		
	}
	
	

	private void msCheck(int t){
		boolean isMS=false;
		int tid=g_js.msCheck(t);
		if(tid==-1) 
			return;
		double prob=g_rutil.getDbl();
		if(prob<g_sm.getMS_Prob())
			isMS=true;
		if(isMS){
			Log.prn(isPrnMS,1, "t:"+t+" mode-switch "+tid);
			g_needRecover=true;
			mode_switch(tid);
		} else {
			g_js.getJM().removeCur();
		}
		
	}
	
	private void relCheck(int t){
		
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				Log.prnc(isSchTab,1,"-");
				continue;
			}
//			Log.prn(2, "rc:"+t);
			if(!tsk.is_HI){
				if(tsk.is_moved){
					Log.prnc(isSchTab,1,"-");
					continue;
				}
				g_si.rel++;
//				Log.prn(2, "rel:"+t);
				if(tsk.is_dropped){
					g_si.drop++;
//					Log.prn(1, "drop plus:"+tsk.tid+" "+g_si.drop);
					Log.prnc(isSchTab,1,"-");
					continue;
				}
			}
			Log.prnc(isSchTab,1,"+");
			g_js.add(relJob_base(tsk,t));
		}
		Log.prnc(isSchTab,1, " ");
	}
	

	
	public void mode_switch(int tid){
		g_si.ms++;
//		Log.prn(9, "a"+g_si.ms);
		modeswitch_in(tid);
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
	
	// abstract method
	protected abstract void initMode_in();
	protected abstract void recover_in();
	protected abstract void modeswitch_in(int tid);
	
	// base instruction
	public void initMode_base_hi() {
		for(Task t:g_tm.getTasks()){
			if(t.is_HI){
//				Log.prn(2, "h:"+t.getHiUtil()+" l:"+t.getLoRUtil());
				if(t.getHiUtil()<t.getLoRUtil())
					t.is_HM=true;
				else
					t.is_HM=false;
//				Log.prn(2, "m:"+t.is_HM);
				
			} else {
				t.is_dropped=false;
			}
		}

//		g_tm.prnHI();
	}
	protected void initMode_base() {
		for(Task t:g_tm.getTasks()){
			if(!t.is_HI)
				t.is_dropped=false;
			else
				t.is_HM=false;
		}

	}
	
	protected Job relJob_base(Task tsk, int t) {
		if(tsk.is_HI){
//			tsk.prnStat();
			if(tsk.is_HM){
				return new Job(tsk.tid, 
						t+tsk.period,tsk.c_h,t+tsk.period,0);
			} else {
				return new Job(tsk.tid, 
						t+tsk.period,tsk.c_l,
						t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new Job(tsk.tid,t+tsk.period,tsk.c_l);
	}
	protected void modeswitch_in_base(int tid){
		Log.prn(isPrnMS,1, "ms hi "+tid);
		g_js.getJM().modeswitch(tid);
		Task tsk=g_tm.getTask(tid);
		if(!tsk.is_HI)	{
			Log.prn(9, "ERROR: task "+tsk.tid+" is not HI-task, cannot mode switch");
			System.exit(0);
		}
		tsk.ms();
	}
	
	public void dropTask_base(Task tsk) {
		if(tsk.is_HI)	{
			Log.prn(9, "ERROR: task "+tsk.tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		if(tsk.is_dropped)
			return;
		int n=g_js.getJM().drop(tsk.tid);
		if(n>1){
			Log.prn(9, "drop num>1");
			System.exit(1);
		}
		if(!tsk.is_moved)
			g_si.drop+=n;
		tsk.drop();
	}
	
	
}
