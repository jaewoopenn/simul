package simul;


import exp.Job;
import exp.JobSimul;
import basic.Task;
import basic.TaskMng;
import utill.Log;
import utill.RUtil;

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
	public void init() {
		g_js=new JobSimul();
		g_si=new SimulInfo();
		g_isMS=false;
	}
	public TaskSimul(TaskMng m){
		this();
		this.g_tm=m;
	}
	public void set_tm(TaskMng tm) {
		this.g_tm = tm;
//		tm.prn();
	}

	public int simulEnd(int st, int et) {
		int ret=simulBy(st,et);
		if(ret==0)
			return 0;
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
		int cur_t=st;
		while(cur_t<et){
			msCheck(cur_t);
			if (!g_js.dlCheck(cur_t)) return 0;
			relCheck(cur_t);
			if(!g_js.progress(cur_t,isSchTab)&&g_isMS) {
				if(isPrnMS)
					Log.prn(1, "recover "+cur_t);
				g_isMS=false;
				initMode();
			}
			if(isSchTab)
				Log.prn(1, " "+cur_t);
			cur_t++;
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
		for(Task t:g_tm.getTasks().getArr()){
			if(!t.is_HI)
				t.is_dropped=false;
			else
				t.is_HM=false;
		}

//		g_tm.prnHI();
	}

	private void msCheck(int cur_t){
		boolean isMS=false;
		int tid=g_js.msCheck(cur_t);
		if(tid==-1) return;
		double prob=g_rutil.getDbl();
		if(prob<g_tm.getInfo().getProb_ms())
			isMS=true;
		if(isMS){
			if(isPrnMS)
				Log.prn(1, "t:"+cur_t+" mode-switch "+tid);
			g_isMS=true;
			modeswitch(tid);
		} else {
			g_js.getJM().removeCur();
		}
		
	}
	
	private void relCheck(int cur_t){
		
		for(int i=0;i<g_tm.getInfo().getSize();i++){
			Task tsk=g_tm.getTask(i);
			if (cur_t%tsk.period!=0){
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
			g_js.add(relJob(tsk,cur_t));
		}
		if(isSchTab)
			Log.prnc(1, " ");
	}
	
	protected abstract Job relJob(Task tsk, int cur_t);

	protected Job relJobD(Task tsk, int cur_t) {
		if(tsk.is_HI){
			if(tsk.is_HM){
				return new Job(tsk.tid, 
						cur_t+tsk.period,tsk.c_h,cur_t+tsk.period,0);
			} else {
				return new Job(tsk.tid, 
						cur_t+tsk.period,tsk.c_l,
						cur_t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new Job(tsk.tid,cur_t+tsk.period,tsk.c_l);
	}
	
	public void modeswitch(int tid){
		Task tsk=g_tm.getTask(tid);
		g_si.ms++;
		if(!tsk.is_HI) 	{
			Log.prn(9, "task "+tid+" is not HI-task, cannot mode-switch");
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
	
	public void dropTask(Task t) {
		if(t.is_HI)	{
			Log.prn(9, "task "+t.tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		if(t.is_dropped)
			return;
		if(isPrnMS)
			Log.prn(1, "drop "+t.tid);
		g_si.drop+=g_js.getJM().drop(t.tid);
		t.drop();
	}
	
	
	// get param
	public SimulInfo getSI(){
		return g_si;
	}
	public TaskMng getTM(){
		return g_tm;
	}
}
