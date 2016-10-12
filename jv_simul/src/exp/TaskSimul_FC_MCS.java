package exp;


import comp.Comp;
import comp.CompMng;
import basic.Task;
import basic.TaskMng;
import utilSim.Log;
import utilSim.MUtil;

public class TaskSimul_FC_MCS extends TaskSimul{

	private CompMng g_cm;
	public TaskSimul_FC_MCS() {
		super();
	}

	public TaskSimul_FC_MCS(TaskMng m) {
		super(m);
	}


	public void set_cm(CompMng cm) {
		this.g_cm = cm;
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
		int cid=g_tm.getComp(tid);
		dropDecision(cid);
		resManager(cid);
	}
	
	private void resManager(int ex_id) {
		double ru=g_cm.getRU();
		Log.prn(1, "G_RU:"+ru);
		double req=ru-1;
		for(Comp c:g_cm.getComps()){
			Log.prn(1, "req:"+req);
			if(req<=0) break;
			double ori=c.getRU();
			double mod=c.request(req);
			req-=ori-mod;
			if(req<=0) break;
		}
		ru=g_cm.getRU();
		Log.prn(1, "G_RU:"+ru);
		System.exit(1);
		
	}

	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		double ru=c.getRU();
		double maxRes=c.getMaxRes();
		Log.prnc(1, "cid:"+cid+" ");
		Log.prnc(1, "max:"+maxRes+" ");
		Log.prn(1, "C_RU:"+ru);
		while(ru>=maxRes){
			int id=c.getTM().findDropTask();
			if(id==-1){
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			drop(id);
			if(isPrnMS)
				Log.prn(1, "drop "+id);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(id);
//			Log.prn(1, ""+ru);
			
		}
		Log.prn(1, ""+ru);
		
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
