package comp;


import basic.Task;
import basic.TaskMng;
//import utill.Log;
import util.MUtil;

public class TaskSimul_FC_MCS extends TaskSimul_FC{


	public TaskSimul_FC_MCS(TaskMng m) {
		super(m);
	}
	
	@Override
	protected void initMode() {
		initMode_base_hi();
	}


	
	
	@Override
	public void modeswitch_in(Task t) {
		modeswitch_in_base(t);		
		int cid=g_tm.get_comp(t.tid);
		dropDecision(cid);
		resManager(cid);
//		System.exit(0);
	}
	
	private void resManager(int ex_id) {
		double ru=g_cm.getRU();
//		Log.prn(1, "G_RU:"+ru);
		double req=ru-1;
		for(Comp c:g_cm.getComps()){
//			Log.prn(1, "req:"+req);
			if(req<=0) break;
			if(c.getID()==ex_id)
				continue;
			double ori=c.getRU();
			double mod=request(c,req);
//			double mod=c.request(req);
			req-=ori-mod;
			if(req<=0) break;
		}
		ru=g_cm.getRU();
//		Log.prn(1, "G_RU:"+ru);
//		System.exit(1);
		
	}


	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		drop_in(c,c.getMaxRes(),false);		
	}
	public double request(Comp c,double req) {
		double ru=c.getRU();
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
				dropTask_base(t);
			}
			else
				return ru;
			
		}
		
	}
	
}
