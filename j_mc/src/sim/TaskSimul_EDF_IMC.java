package sim;


import job.Job;
import task.Task;

public class TaskSimul_EDF_IMC extends TaskSimul{

	public TaskSimul_EDF_IMC() {
		super();
		g_name="EDF-IMC";
	}
	@Override
	protected void initSimul() {
		
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
	}



	@Override
	protected Job rel_one_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.vd)+" "+tsk.c_l+" "+tsk.isHC());
		int dl=t+tsk.period;
		return new Job(tsk.tid,dl,Math.max(tsk.c_h,tsk.c_l),0);
	}

	@Override
	protected void changeVD_nextSt() {
		
	}

	@Override
	protected void setDelay() {
		
	}


}
