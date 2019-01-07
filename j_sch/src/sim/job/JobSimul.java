package sim.job;


import util.FLog;
import util.Log;

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
		work();
		g_t++;
	}
	private void work(){
		FLog.prnc( String.format("%05d ", g_t));
		if(!dlCheck(g_t))
			System.exit(1);
		progress();

	}
	

	public void simulEnd(){
		if(g_jm.endCheck(g_t)==0){
			g_jm.prn();
			Log.prn(9,"Deadline miss at time "+g_t);
			System.exit(1);
		}
		FLog.prn("*** Left Jobs at time "+g_t+" ***");
		g_jm.f_prn();
	}
	

	
	

	
	
	
	private boolean dlCheck(int cur_t){
		Job j=g_jm.getCur();
		if(j==null)
			return true;
		if(cur_t<j.dl) 
			return true;
		Log.prn(9,"deadline miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", dl:"+j.dl);
		return false;
	}
	
	protected void progress(){
		Job j=g_jm.getCur();
		if(!FLog.isON())
			return;
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
		s=g_jm.getJobArrow(j,out_type);
		FLog.prn(s);
	}
	
	
	public JobMng getJM() {
		return g_jm;
	}
	public boolean isIdle() {
		if(g_jm.getCur()==null)
			return true;
		return false;
	}
	public int getTime() {
		return g_t;
	}

}
