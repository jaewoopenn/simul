package Exp;


import Basic.Task;
import Basic.TaskMng;
import Util.Log;

public class TaskSimul {
	TaskMng g_tm;
	JobSimul g_js;
	public TaskSimul(TaskMng m){
		g_tm=m;
	}
	public void init() {
		g_js=new JobSimul();
		Log.prn(1, "rel  / exec / t");
//		g_js.addJob(0, 4, 2);
//		g_js.addJob(1, 5, 1);
//		g_js.simul(5);
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
			if(tsk.is_HI)
				g_js.add(new Job(tsk.tid,cur_t+(int)Math.ceil(tsk.vd),tsk.c_l));
			else
				g_js.add(new Job(tsk.tid,cur_t+tsk.period,tsk.c_l));
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
	public void prn() {
		g_tm.prn();
	}
	public void prepareMC() {
		g_tm.setVD(0, 3);
	}
	
	
}
