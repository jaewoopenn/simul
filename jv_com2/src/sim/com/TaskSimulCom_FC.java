package sim.com;


import comp.Comp;
import task.Task;
import task.TaskMng;
import util.MUtil;
import util.SLog;

public class TaskSimulCom_FC extends TaskSimulCom{


	public TaskSimulCom_FC() {
		super();
		g_name="Com_FC";
	}
	@Override
	public void initSimul() {
		super.initSimul();
		
	}

	
	
	@Override
	protected void modeswitch_in(Task tsk) {
		modeswitch_after(tsk);		
		int cid=tsk.getComp();
		dropDecision(cid);
		resManager(cid);
	}
	private void resManager(int ex_id) {
		double ru=g_cm.getVU();
		double req=ru-1;
		if(ru<1) 
			return;
		SLog.prn(1, "G_RU:"+ru);
		for(Comp c:g_cm.getComps()){
			SLog.prn(1, "req:"+req);
			if(req<=0) break;
			if(c.getID()==ex_id)
				continue;
			double ori=c.getVU();
			double mod=request(c,req);
			req-=ori-mod;
			if(req<=0) break;
//			SLog.prn(1, "G_RU:"+ru);
//			System.exit(1);
		}
		ru=g_cm.getRU();
		
	}
	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		drop_in(c,c.getMaxRes(),false);		
	}
	public double request(Comp c,double req) {
		double ru=c.getVU();
		double tu=ru-req;
		return drop_in(c,tu,true);
	}
	
	private double drop_in(Comp c, double lim, boolean isShared) {
		Task t;
		TaskMng tm=c.getTM();
		while(true){
			double ru=tm.getRUtil();
//			Log.prn(1, "RU:"+ru);
			if(ru>lim+MUtil.err){
				if(isShared)
					t=tm.findDropTask_shared();
				else
					t=tm.findDropTask();
				if(t==null) 
					return ru;
				drop_task(t);
			}
			else
				return ru;
			
		}
		
	}

	

	@Override
	protected void recover_in(int tid) {
		
	}









}
