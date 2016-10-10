package basic;


import utilSim.Log;

public class TaskGenMC extends TaskGen {
	
	public TaskGenMC(TaskGenParam tgp) {
		super(tgp);
	}




	@Override
	public Task genTask(int tid){
		Task tsk=g_param.genTask(tid,true);
		if(!g_param.chkTask(tsk))
			return null;
		if(!g_param.chkMCTask(tsk))
			return null;
		return tsk;
	}



	
	@Override
	public void prn(int lv) {
		for(Task t:g_tasks) {
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+
					", h:"+t.c_h+", Xi:"+t.is_HI);
		}
		Log.prn(lv, "MC util:"+getUtil());
			
	}


	@Override
	protected double getUtil(){
		double loutil=0;
		double hiutil=0;
		for(Task t:g_tasks){
			loutil+=(double)(t.c_l)/t.period;
			if(t.is_HI)
				hiutil+=(double)(t.c_h)/t.period;
		}
		return Math.max(loutil, hiutil);
	}

	
}
