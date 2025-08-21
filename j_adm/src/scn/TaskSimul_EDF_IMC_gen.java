package scn;


import imc.TaskSimul;
import sim.job.Job;
import task.Task;

public class TaskSimul_EDF_IMC_gen extends TaskSimul{

	public TaskSimul_EDF_IMC_gen() {
		super();
		g_name="EDF-IMC";
	}
	


	@Override
	public void initSimul() {
		
	}

	@Override
	protected void modeswitch_in(Task tsk) {
		for(Task t:g_tm.getTasks()){
			if(t.isHC()){
				g_jsm.getJM().modeswitch(tsk.tid);
				t.ms();
			} else {
				g_ts.degrade_task(t);
			}
		}
	}

	@Override
	protected Job rel_one_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.vd)+" "+tsk.c_l+" "+tsk.isHC());
		int dl=t+tsk.period;
		if(!tsk.isHC()) {
			return new Job(tsk.tid,dl,0,0);
		}

		// HC task 
		Job j;
		if(tsk.isHM()){ // HI-mode
			j= new Job(tsk.tid, dl, tsk.c_h,dl,0);
		} else { // LO-mode
			j= new Job(tsk.tid, dl,tsk.c_l,t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
		}
		return j;
	}

	@Override
	protected void changeVD_nextSt() {
		g_tm.setX(g_sm.getX());
		
	}



	@Override
	protected void setDelay() {
		
	}


}
