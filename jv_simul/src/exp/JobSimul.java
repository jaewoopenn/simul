package exp;


import utilSim.Log;

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

	public int simulDur(int st, int et){
		int cur_t=st;
		while(cur_t<et){
			if (work(cur_t)==0) return 0;
			cur_t++;
		}
		return 1;
	}
	public int simulEnd(int cur_t){
		Log.prn(1, "*** Left Jobs at time "+cur_t+" ***");
		g_jm.prn();
		return g_jm.endCheck(cur_t);
		
	}
	public int work(int cur_t){
		if(!g_jm.dlCheck(cur_t)) return 0;
		if(!progress(cur_t)) return 0;
		return 1;
		
	}
	public boolean progress(int cur_t){
		Job j=g_jm.getCur();
		int out_type=0;
		if(j==null)
		{
			g_jm.prnJob(null,out_type);
			return true;
		}
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			g_jm.removeCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		g_jm.prnJob(j,out_type);
		return true;
	}
	public JobMng getJM() {
		return g_jm;
	}
}
