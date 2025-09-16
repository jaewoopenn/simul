package job;


import util.SLog;
import util.SLogF;

public class JobSimul {
	protected JobMng g_jm;
	protected int g_t;
	public JobSimul(){
		g_t=0;
		g_jm=new JobMng();
	}
	
	
	public JobMng getJM() {
		return g_jm;
	}
		
	protected void exec_one(){
		Job j=g_jm.getCur();
		String s="t:"+g_t+" ";
		if(j==null)	{
			s+="idle";
		}
		else if(j.exec<=1) {
			j.exec=0;
			// removing is processing in ms_check 
			s+="complete "+ j.tid;
			g_jm.removeCur();
		} else {  // j.exec>1
			j.exec-=1;
			s+="exec "+j.tid;
		}
		SLog.prn(s);
//		SLogF.prn(s);
	}	
	
	

	
	public void add(Job j){
		g_jm.add(j);
	}


	public int simul_one() {
		SLogF.prnc( String.format("%05d ", g_t));
		int dl=dl_check(g_t);
		exec_one();
		g_t++;
		return dl;
	}
	

	public int simul_end(){
		if(g_jm.endCheck(g_t)==0){
			g_jm.prn();
			SLog.err("Deadline miss at time "+g_t);
		}
		SLogF.prn("*** Left Jobs at time "+g_t+" ***");
		return g_jm.endDL(g_t);
	}
	

	
	

	
	
	
	private int dl_check(int cur_t){
		int dm=0;
		while(true) {
			Job j=g_jm.getCur();
			if(j==null)
				break;
			if(cur_t<j.dl) 
				break;
			SLog.err("Job Simul: DL miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+
					", dl:"+j.dl);
				
		}

		return dm;
	}
	
	public boolean is_idle() {
		if(g_jm.isIdle())
			return true;
		return false;
	}
	public int get_time() {
		return g_t;
	}

	
}
