package gen;


import basic.Task;
import util.Log;

public class TaskGenNonMC extends TaskGen {
	
	public TaskGenNonMC(TaskGenParam tgp) {
		super(tgp);
	}



	@Override
	public Task genTask(int tid){
		Task tsk=g_param.genTask(tid,false);
		if(!g_param.chkTask(tsk))
			return null;
		return tsk;
	}


	
	@Override
	public void prn(int lv) {
		for(Task t:g_tasks) {
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l);
		}
		Log.prn(lv, "Util:"+getUtil());
			
	}



	@Override
	protected double getUtil() {
		double util=0;
		for(Task t:g_tasks){
			util+=t.getHiUtil();
		}
		return util;
	}


	
}
