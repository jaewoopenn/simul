package sim.mc;


import basic.Task;
import sim.SimulInfo;
import sim.TaskSimul;
import sim.job.Job;
import util.Log;

public abstract class TaskSimulMC extends TaskSimul {

	protected boolean g_needRecover=false;
	public boolean g_recoverOn=true;
	public boolean isPrnMS=true;
	protected JobSimulMC g_jsm;


	protected void init() {
		g_jsm=new JobSimulMC();
		g_js=g_jsm;

		g_si=new SimulInfo();
		g_needRecover=false;
	}

	
	public void simul_t(){
		msCheck();
		relCheck();
		g_js.simul_one();
		if(g_js.isIdle()&&g_needRecover&&g_recoverOn) {
			recover();
		}
		//Log.prn(isSchTab,1, " "+t);
	}

	private void recover(){
//		Log.prn(isPrnMS,1, "recover "+t);
		g_needRecover=false;
		recover_in();
		initMode_in();
//		System.exit(0);
		
	}
	
	

	private void msCheck(){
		boolean isMS=false;
		int tid=g_jsm.msCheck();
		if(tid==-1) 
			return;
		double prob=g_rutil.getDbl();
		if(prob<g_sm.getMS_Prob())
			isMS=true;
		if(isMS){
			Log.prn(isPrnMS,1, "t:"+g_js.getTime()+" mode-switch "+tid);
			g_needRecover=true;
			mode_switch(tid);
		} else {
			g_js.getJM().removeCur();
		}
		
	}
	
	private void relCheck(){
		int t=g_js.getTime();
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				Log.prnc(isSchTab,1,"-");
				continue;
			}
//			Log.prn(2, "rc:"+t);
			if(!tsk.is_HI){
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
		g_jsm.getJM().modeswitch(tid);
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
		int n=g_jsm.getJM().drop(tsk.tid);
		if(n>1){
			Log.prn(9, "drop num>1");
			System.exit(1);
		}
		g_si.drop+=n;
		tsk.drop();
	}
	
	
}
