package exp;


import comp.Comp;
import comp.CompMng;
import basic.Task;
import basic.TaskMng;
import utilSim.Log;
import utilSim.MUtil;

public class TaskSimul_FC_Naive extends TaskSimul{
	private boolean isExtMS=false;
	private CompMng g_cm;
	public TaskSimul_FC_Naive() {
		super();
	}

	public TaskSimul_FC_Naive(TaskMng m) {
		super(m);
	}


	public void set_cm(CompMng cm) {
		this.g_cm = cm;
	}

	@Override
	protected void initMode() {
		initModeS();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		modeswitch_in_pre(tid);		
		int cid=g_tm.getComp(tid);
		dropDecision(cid);
		resManager(cid);
	}
	
	private void resManager(int ex_id) {
		if(isExtMS) return;
		for(Comp c:g_cm.getComps()){
			if(c.getID()==ex_id)
				continue;
			dropAll(c,false); // only shared
		}
		isExtMS=true;
	}


	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		dropAll(c,true);
	}
	public void dropAll(Comp c,boolean isAll) {
		for(Task t:g_tm.getLoTasks()){
			if(isAll||!t.is_isol())
				t.drop();
		}
		
	}
	

	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
