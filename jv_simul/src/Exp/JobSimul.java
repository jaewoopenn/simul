package Exp;


import Basic.Task;
import Basic.TaskMng;
import Util.Log;

public class JobSimul {
	JobMng jm;
	public JobSimul(){
		jm=new JobMng();
	}
	public JobSimul(JobMng m){
		jm=m;
	}
	
	public void addJob(int id, int dl, double et ){
		jm.addJob(id,dl,et);
	}
	public int simul(int et){
		int cur_t=0;
		while(cur_t<et){
			if (work(cur_t)==0) return 0;
			cur_t++;
		}
		Log.prn(1, "*** Left Jobs at time "+cur_t+" ***");
		jm.prn();
		return jm.endCheck(et);
	}

	private int work(int cur_t){
		if(!jm.dlCheck(cur_t)) return 0;
		Log.prnc(1,"t:"+cur_t+" ");
		if(!progress(cur_t)) return 0;
		return 1;
		
	}
	public boolean progress(int cur_t){
		Job j=jm.getCur();
		int out_type=0;
		if(j==null)
		{
			jm.prnJob(null,out_type);
			return true;
		}
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			jm.pollCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		jm.prnJob(j,out_type);
		return true;
		
	}

}
