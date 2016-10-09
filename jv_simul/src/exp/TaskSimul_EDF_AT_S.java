package exp;


import basic.Task;
import basic.TaskMng;
import utilSim.Log;
import utilSim.MUtil;

public class TaskSimul_EDF_AT_S extends TaskSimul{

	public TaskSimul_EDF_AT_S() {
		super();
	}

	public TaskSimul_EDF_AT_S(TaskMng m) {
		super(m);
	}


	protected void initMode() {
		Task[] tasks=g_tm.getTasks();
		for(Task t:tasks){
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
	
	
	public void modeswitch_in(int tid) {
		if(isPrnMS)
			Log.prn(1, "ms hi "+tid);
		g_js.getJM().modeswitch(tid);
		g_tm.modeswitch(tid);
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRU();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			int id=g_tm.findDropTask();
			if(id==-1){
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			drop(id);
			if(isPrnMS)
				Log.prn(1, "drop "+id);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(id);
		}
//		Log.prn(1, ""+ru);
		
	}

	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
		if(tsk.is_HI){
			if(tsk.is_HM){
				return new JobD(tsk.tid, 
						cur_t+tsk.period,tsk.c_h,cur_t+tsk.period,0);
			} else {
				return new JobD(tsk.tid, 
						cur_t+tsk.period,tsk.c_l,
						cur_t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new JobD(tsk.tid,cur_t+tsk.period,tsk.c_l);
	}
	
	
	
	
	// get param
}
