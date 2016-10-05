package exp;


import basic.Task;
import basic.TaskMng;
import utilSim.Log;
import utilSim.MUtil;
import utilSim.RUtil;

public class TaskSimul {
	private int g_Rel=0;
	private int g_Drop=0;
	
	private TaskMng g_tm;
	private JobSimul g_js;
	private RUtil g_rutil=new RUtil();
	public boolean isSchTab=true;
	public boolean isPrnMS=true;
	public TaskSimul(TaskMng m){
		g_tm=m;
		g_js=new JobSimul();
	}
	public TaskMng getTM(){
		return g_tm;
	}
	public int simulEnd(int st, int et) {
		int ret=simulBy(st,et);
		if(ret==0)
			return 0;
		return g_js.simulEnd(et);
	}
	
	public int simulBy(int st, int et){
		if(st==0){
			if(isSchTab)
				Log.prn(1, "rel  / exec / t");
		}
		int cur_t=st;
		while(cur_t<et){
			msCheck(cur_t);
			if (!g_js.dlCheck(cur_t)) return 0;
			relCheck(cur_t);
			if(!g_js.progress(cur_t,isSchTab)) return 0;
			if(isSchTab)
				Log.prn(1, " "+cur_t);
			cur_t++;
		}
		return 1;
	}
	private void msCheck(int cur_t){
		boolean isMS=false;
		int tid=g_js.msCheck();
		if(tid==-1) return;
		double prob=g_rutil.getDbl();
		if(prob<g_tm.getInfo().getProb_ms())
			isMS=true;
		if(isMS){
			if(isPrnMS)
				Log.prn(1, "t:"+cur_t+" mode-switch "+tid);
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
				g_Rel++;
				if(tsk.is_dropped){
					g_Drop++;
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
	
	private Job relJob(Task tsk, int cur_t) {
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
	
	public void modeswitch(int tid) {
		Task tsk=g_tm.getTask(tid);
		if(!tsk.is_HI) 	{
			Log.prn(9, "task "+tid+" is not HI-task, cannot mode-switch");
			System.exit(0);
		}
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
	
	public void drop(int tid) {
		Task tsk=g_tm.getTask(tid);
		if(tsk.is_HI)	{
			Log.prn(9, "task "+tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		g_js.getJM().drop(tid);
		g_tm.drop(tid);
	}
	
	public double getDMR(){
		return (double)g_Drop/g_Rel;
	}
}
