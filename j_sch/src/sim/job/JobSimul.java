package sim.job;


import util.SLogF;
import util.SLog;

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
		SLogF.prnc( String.format("%05d ", g_t));
		dl_check(g_t);
		exec_one();
		g_t++;
	}
	

	public void simul_end(){
		if(g_jm.endCheck(g_t)==0){
			g_jm.prn();
			SLog.prn(9,"Deadline miss at time "+g_t);
			System.exit(1);
		}
		SLogF.prn("*** Left Jobs at time "+g_t+" ***");
		g_jm.f_prn();
	}
	

	
	

	
	
	
	private boolean dl_check(int cur_t){
		Job j=g_jm.getCur();
		if(j==null)
			return true;
		if(j.isHI) {
			if(cur_t<j.vd) 
				return true;
			SLog.err("Job Simul: VD miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", vd:"+j.vd);
		}else {
			if(cur_t<j.dl) 
				return true;
			SLog.err("Job Simul: DL miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", dl:"+j.dl);
			
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
		if(!SLogF.isON())
			return;
		s=g_jm.getJobArrow(j,out_type);
		SLogF.prn(s);
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
