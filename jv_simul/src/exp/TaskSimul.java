package exp;


import basic.Task;
import basic.TaskMng;
import utilSim.Log;

public class TaskSimul {
	private TaskMng g_tm;
	private JobSimul g_js;
	public TaskSimul(TaskMng m){
		g_tm=m;
	}
	public void init() {
		g_js=new JobSimul();
		Log.prn(1, "rel  / exec / t");
	}
	public int simulDur(int st, int et){
		int cur_t=st;
		while(cur_t<et){
			relCheck(cur_t);
			if (g_js.work(cur_t)==0) return 0;
			Log.prn(1, " "+cur_t);
			cur_t++;
		}
		return 1;
	}
	private void relCheck(int cur_t){
		
		for(int i=0;i<g_tm.getInfo().getSize();i++){
			Task tsk=g_tm.getTask(i);
			if (cur_t%tsk.period!=0){
				Log.prnc(1,"-");
				continue;
			}
			Log.prnc(1,"+");
			Job j;
			if(tsk.is_HI){
				if(tsk.is_HM)
					j=new Job(tsk.tid, 
							cur_t+(int)Math.ceil(tsk.vd),tsk.c_l);
				else
					j=new Job(tsk.tid, 
							cur_t+(int)Math.ceil(tsk.vd),tsk.c_l);
				
			}
			else
				j=new Job(tsk.tid,cur_t+tsk.period,tsk.c_l);
			g_js.add(j);
		}
		Log.prnc(1, " ");
	}
	public int simulEnd(int t) {
		return g_js.simulEnd(t);
	}
	public int exec(int et) {
		init();
		simulDur(0, et);
		return simulEnd(et);
	}
	public void modeswitch(int tid) {
		Log.prn(1, "mode-switch "+tid);
		g_js.getJM().modeswitch(tid);
		g_tm.modeswitch(tid);
	}
	public TaskMng getTM(){
		return g_tm;
	}
}
