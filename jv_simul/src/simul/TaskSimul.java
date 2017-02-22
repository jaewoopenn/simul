package simul;


import exp.Job;
import exp.JobSimul;
import basic.Task;
import basic.TaskMng;
import util.Log;
import util.RUtil;

public abstract class TaskSimul {
	protected SimulInfo g_si;
	protected TaskMng g_tm;
	protected JobSimul g_js;
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
		g_js=new JobSimul();
		g_si=new SimulInfo();
		g_needRecover=false;
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
		initMode();
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
		g_js.simulEndPrn(isPrnEnd);
		g_js.simulEnd(et);
	}
	private void recover(int t){
		Log.prn(isPrnMS,1, "recover "+t);
		g_needRecover=false;
		recover_in();
		initMode();
//		System.exit(0);
		
	}
	
	

	private void msCheck(int t){
		boolean isMS=false;
		Task tsk=g_js.msCheck(t);
		if(tsk==null) 
			return;
		double prob=g_rutil.getDbl();
		if(prob<g_tm.getInfo().getProb_ms())
			isMS=true;
		if(isMS){
			Log.prn(isPrnMS,1, "t:"+t+" mode-switch "+tsk.tid);
			g_needRecover=true;
			mode_switch(tsk);
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
	

	
	public void mode_switch(Task tsk){
		g_si.ms++;
//		Log.prn(9, "a"+g_si.ms);
		if(!tsk.is_HI) 	{
			Log.prn(9, "ERROR: task "+tsk.tid+" is not HI-task, cannot mode-switch");
			System.exit(0);
		}
		modeswitch_in(tsk);
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
	protected abstract void initMode();
	protected abstract void recover_in();
	protected abstract void modeswitch_in(Task tsk);
	
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
//		g_tm.prn();
		for(Task t:g_tm.getTasks()){
			if(!t.is_HI)
				t.is_dropped=false;
			else
				t.is_HM=false;
		}

//		g_tm.prnHI();
	}
	
	protected Job relJob_base(Task tsk, int t) {
		if(tsk.is_HI){
//			tsk.prnStat();
			if(tsk.is_HM){
				return new Job(tsk, 
						t+tsk.period,tsk.c_h,t+tsk.period,0);
			} else {
				return new Job(tsk, 
						t+tsk.period,tsk.c_l,
						t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new Job(tsk,t+tsk.period,tsk.c_l);
	}
	protected void modeswitch_in_base(Task t){
		Log.prn(isPrnMS,1, "ms hi "+t.tid);
		g_js.getJM().modeswitch(t);
		t.ms();
	}
	
	public void dropTask_base(Task tsk) {
		if(tsk.is_HI)	{
			Log.prn(9, "ERROR: task "+tsk.tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		if(tsk.is_dropped)
			return;
		int n=g_js.getJM().drop(tsk);
		if(n>1){
			Log.prn(9, "drop num>1");
			System.exit(1);
		}
		if(!tsk.is_moved)
			g_si.drop+=n;
		tsk.drop();
	}
	
	
}
