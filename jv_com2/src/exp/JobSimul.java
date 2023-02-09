package exp;


import task.Task;
import util.Log;

public class JobSimul {
	JobMng g_jm;
	public JobSimul(){
		g_jm=new JobMng();
	}
	public JobSimul(JobMng m){
		g_jm=m;
	}
	
	public void add(Job j){
		g_jm.add(j);
	}
	public int simul(int et){
		int cur_t=0;
		while(cur_t<et){
			if (work(cur_t)==0) return 0;
			cur_t++;
		}
		Log.prn(1, "*** Left Jobs at time "+cur_t+" ***");
		g_jm.prn();
		return g_jm.endCheck(et);
	}

	// deprecated 
//	public int simulDur(int st, int et){
//		int cur_t=st;
//		while(cur_t<et){
//			if (work(cur_t)==0) return 0;
//			cur_t++;
//		}
//		return 1;
//	}
//	
	// deprecated 
	private int work(int cur_t){
		Log.prnc(1, cur_t+" ");
		dlCheck(cur_t);
		progress(cur_t,true);
		Log.prn(1, "");
		return 1;
	}

	public void simulEnd(int cur_t){
		if(g_jm.endCheck(cur_t)==0){
			g_jm.prn();
			Log.prn(9,"Deadline miss at time "+cur_t);
			System.exit(1);
		}
	}
	public void simulEndPrn(boolean b){
		if(!b)
			return;
		Log.prn(1, "*** Left Jobs  ***");
		g_jm.prn();
	}
	
	
	public void dlCheck(int cur_t){
		Job j=g_jm.getCur();
		if(j==null)
			return;
		if(cur_t<j.dl) 
			return;
		Log.prn(9,"deadline miss at time "+cur_t+": tid:"+j.tsk.tid+", left exec:"+(j.exec)+", dl:"+j.dl);
		System.exit(1);
	}
	public Task msCheck(int cur_t) { // before dlcheck
		Job j;
		while(true){
			j=(Job)g_jm.getCur();
			if(j==null)
				return null;
			if(j.exec==0){
				if(j.isHI==true){
					if(cur_t>j.vd){
						Log.prn(9, "vd miss"+j.tsk.tid);
						System.exit(1);
					}
					return j.tsk;
				}
				else
					g_jm.removeCur();
				
			}
			else
				break;
		}
		return null;
	}
	
	public boolean progress(int cur_t, boolean b){
		Job j=g_jm.getCur();
		int out_type=0;
		if(j==null)	{
			g_jm.prnJob(true,null,out_type);
			return false;
		}
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			if (!j.isHI||j.add_exec==0)
				g_jm.removeCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		g_jm.prnJob(b,j,out_type);
		return true;
	}
	public JobMng getJM() {
		return g_jm;
	}
}
