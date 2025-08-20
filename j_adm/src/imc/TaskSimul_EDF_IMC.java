package imc;


import sim.job.Job;
import task.Task;

public class TaskSimul_EDF_IMC extends TaskSimul{

	public TaskSimul_EDF_IMC() {
		super();
		g_name="EDF-IMC";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
	}


	@Override
	public void initSimul() {
		
	}

	@Override
	protected Job rel_one_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.vd)+" "+tsk.c_l+" "+tsk.isHC());
		int dl=t+tsk.period;
		return new Job(tsk.tid,dl,Math.max(tsk.c_h,tsk.c_l),0);
	}

	@Override
	protected void setVD() {
		g_tm.setX(g_sm.getX());
		
	}

	@Override
	protected void setDelay() {
		
	}


}
