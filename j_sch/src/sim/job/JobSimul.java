package sim.job;


import util.S_FLog;
import util.S_Log;

public class JobSimul {
	protected JobMng g_jm;
	protected int g_t;
	public JobSimul(int n){
		if(n!=0)
			g_jm=new JobMng(n);
		g_t=0;
	}
	
	
	public void add(Job j){
		g_jm.add(j);
	}


	public void simul_one() {
		S_FLog.prnc( String.format("%05d ", g_t));
		dl_check(g_t);
		exec_one();
		g_t++;
	}
	

	public void simul_end(){
		if(g_jm.endCheck(g_t)==0){
			g_jm.prn();
			S_Log.prn(9,"Deadline miss at time "+g_t);
			System.exit(1);
		}
		S_FLog.prn("*** Left Jobs at time "+g_t+" ***");
		g_jm.f_prn();
	}
	

	
	

	
	
	
	private boolean dl_check(int cur_t){
		Job j=g_jm.getCur();
		if(j==null)
			return true;
		if(j.isHI) {
			if(cur_t<j.vd) 
				return true;
			S_Log.err("Job Simul: VD miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", vd:"+j.vd);
		}else {
			if(cur_t<j.dl) 
				return true;
			S_Log.err("Job Simul: DL miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", dl:"+j.dl);
			
		}

		return false;
	}
	
	protected void exec_one(){
		Job j=g_jm.getCur();
		int out_type=0;
		String s="";
		if(j==null)	{

		}
		else if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			g_jm.removeCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		
		// File Log
		if(!S_FLog.isON())
			return;
		s=g_jm.getJobArrow(j,out_type);
		S_FLog.prn(s);
	}
	
	
	public JobMng getJM() {
		return g_jm;
	}
	public boolean is_idle() {
		if(g_jm.getCur()==null)
			return true;
		return false;
	}
	public int get_time() {
		return g_t;
	}

}
