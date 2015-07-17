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
	public void simul(int et){
		while(cur_t<et){
//			Log.prn(2,"t:"+cur_t);
			relCheck();
			jm.progress(cur_t,1);
			cur_t++;
		}
		jm.prn();
	}

	
	private void relCheck(){
		for(int i=0;i<tm.size();i++){
			if (cur_t%plst[i]==0){
//				Log.prn(2,"rel "+i);
				Task tsk=tm.getTask(i);
//				Log.prn(2, "p:"+tsk.period+" e:"+tsk.exec);
				jm.insertJob(tsk.tid,cur_t+tsk.period,tsk.exec);
			}
				
		}
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
