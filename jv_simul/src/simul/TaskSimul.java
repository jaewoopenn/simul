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
	protected boolean g_isMS=false;
	public boolean isSchTab=true;
	public boolean isPrnMS=true;
	public boolean isPrnEnd=true;

	public TaskSimul(){
		init();
	}
	public TaskSimul(TaskMng m){
		this();
		this.g_tm=m;
	}
	public void init() {
		g_js=new JobSimul();
		g_si=new SimulInfo();
		g_isMS=false;
	}
	public void checkErr() {
		if(g_tm==null){
			Log.prn(9, "ERROR: TaskMng is not set");
			System.exit(1);
		}
		
	}
	
	public void set_tm(TaskMng tm) {
		this.g_tm = tm;
//		tm.prn();
	}

	public int simulEnd(int st, int et) {
		int ret=simulBy(st,et);
		if(ret==0){
			Log.prn(9, "ERROR: ExpSimul");
			System.exit(1);
		}
		if(isPrnEnd)
			g_js.simulEndPrn();
		return g_js.simulEnd(et);
	}
	
	public int simulBy(int st, int et){
		if(st==0){
			if(isSchTab)
				Log.prn(1, "rel  / exec / t");
			initMode();
		}
		int t=st;
		while(t<et){
			msCheck(t);
			if (!g_js.dlCheck(t)) return 0;
			relCheck(t);
			if(!g_js.progress(t,isSchTab)&&g_isMS) {
				if(isPrnMS)
					Log.prn(1, "recover "+t);
				g_isMS=false;
				initMode();
			}
			if(isSchTab)
				Log.prn(1, " "+t);
			t++;
		}
		return 1;
	}

	protected abstract void initMode();
	
	public void initModeS() {
		for(Task t:g_tm.getTasks().getArr()){
			if(t.is_HI){
				if(t.getHiUtil()<t.getLoRUtil())
					t.is_HM=true;
				else
					t.is_HM=false;
			} else {
				t.is_dropped=false;
			}
		}

//		g_tm.prnHI();
	}
	protected void initModeN() {
//		g_tm.prn();
		for(Task t:g_tm.getTasks().getArr()){
			if(!t.is_HI)
				t.is_dropped=false;
			else
				t.is_HM=false;
		}

//		g_tm.prnHI();
	}

	private void msCheck(int t){
		boolean isMS=false;
		int tid=g_js.msCheck(t);
		if(tid==-1) return;
		double prob=g_rutil.getDbl();
		if(prob<g_tm.getInfo().getProb_ms())
			isMS=true;
		if(isMS){
			if(isPrnMS)
				Log.prn(1, "t:"+t+" mode-switch "+tid);
			g_isMS=true;
			modeswitch(tid);
		} else {
			g_js.getJM().removeCur();
		}
		
	}
	
	private void relCheck(int t){
		
		for(int i=0;i<g_tm.getInfo().getSize();i++){
			Task tsk=g_tm.getTask(i);
			if (t%tsk.period!=0){
				if(isSchTab)
					Log.prnc(1,"-");
				continue;
			}
			if(!tsk.is_HI){
				g_si.rel++;
				if(tsk.is_dropped){
					g_si.drop++;
//					Log.prn(1, "drop plus:"+tsk.tid+" "+g_si.drop);
					if(isSchTab)
						Log.prnc(1,"-");
					continue;
				}
			}
			if(isSchTab)
				Log.prnc(1,"+");
			g_js.add(relJob(tsk,t));
		}
		if(isSchTab)
			Log.prnc(1, " ");
	}
	
	protected abstract Job relJob(Task tsk, int t);

	protected Job relJobD(Task tsk, int t) {
		if(tsk.is_HI){
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
	
	public void modeswitch(int tid){
		Task tsk=g_tm.getTask(tid);
		g_si.ms++;
		if(!tsk.is_HI) 	{
			Log.prn(9, "ERROR: task "+tid+" is not HI-task, cannot mode-switch");
			System.exit(0);
		}
		modeswitch_in(tid);
	}
	protected abstract void modeswitch_in(int tid);
	protected void modeswitch_in_pre(int tid){
		if(isPrnMS)
			Log.prn(1, "ms hi "+tid);
		g_js.getJM().modeswitch(tid);
		g_tm.getTask(tid).ms();
	}
	
	public void dropTask(Task tsk) {
		if(tsk.is_HI)	{
			Log.prn(9, "ERROR: task "+tsk.tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		if(tsk.is_dropped)
			return;
		if(isPrnMS)
			Log.prn(1, "drop "+tsk.tid);
		g_si.drop+=g_js.getJM().drop(tsk.tid);
		tsk.drop();
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
}
