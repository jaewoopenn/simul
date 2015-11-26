package Simul;

import java.util.Vector;

import Util.Log;

public class Platform {
	TaskMng tm;
	JobMng jm;
	int cur_t;
	int[] plst;
	
	public void init(TaskMng mng) {
		tm=mng;
		tm.finalize();
		plst=tm.getPeriods();
		
		jm=new JobMng();
		cur_t=0;
	}
	public int simul(int et){
		boolean bSuc;
		while(cur_t<et){
			bSuc=jm.dlCheck(cur_t);
			if(!bSuc) return 0;
			Log.prnc(1,"t:"+cur_t+" rel:");
			relCheck();
			bSuc=jm.progress(cur_t);
			if(!bSuc) return 0;
			cur_t++;
		}
		Log.prnc(1, "Left Job:");
		jm.prn();
		return jm.endCheck(et);
	}

	
	private void relCheck(){
		
		for(int i=0;i<tm.size();i++){
			if (cur_t%plst[i]==0){
//				Log.prn(2,"rel "+i);
				Task tsk=tm.getTask(i);
//				Log.prn(2, "p:"+tsk.period+" e:"+tsk.exec);
				jm.insertJob(tsk.tid,cur_t+tsk.period,tsk.c_l);
				Log.prnc(1,"+");
			} else {
				Log.prnc(1,"-");
			}
		}
		Log.prnc(1, " exe:");
	}

//	private int computeLCM() {
//		
//		for(Task t:tasks)
//		{
//			Log.prn(t.period);
//		}
//		return 0;
//	}

}
