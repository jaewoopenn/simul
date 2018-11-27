package sim.job;


import util.Log;

public class JobSimul {
	protected JobMng g_jm;
	protected int g_t;
	protected boolean isVisible=true;
	public JobSimul(){
		g_jm=new JobMng();
		g_t=0;
	}
	public void setJM(JobMng jm) {
		g_jm=jm;
	}
	public void setVisible(boolean b) {
		isVisible=b;
		
	}
	
	
	public void add(Job j){
		g_jm.add(j);
	}

	// all in one
	public int simul(int et){
		simulBy(et);
		simulEnd();
		return g_jm.endCheck(et);
	}
	public void simul_one() {
		simulBy(g_t+1);
	}
	
	public int simulBy(int et){
		while(g_t<et){
			work();
			g_t++;
		}
		return 1;
	}
	public void simulEnd(){
		if(g_jm.endCheck(g_t)==0){
			g_jm.prn();
			Log.prn(9,"Deadline miss at time "+g_t);
			System.exit(1);
		}
		if(!isVisible)
			return;
		Log.prn(1, "*** Left Jobs at time "+g_t+" ***");
		g_jm.prn();
	}
	
	private void work(){
		Log.prnc(isVisible,1, g_t+" ");
		if(!dlCheck(g_t))
			System.exit(1);
		progress();

		Log.prn(isVisible,1, "");
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
		int out_type=0;
		if(j==null)	{
			g_jm.prnJob(true,null,out_type);
			return;
		}
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			g_jm.removeCur();
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		g_jm.prnJob(isVisible,j,out_type);
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
