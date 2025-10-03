package job;



import util.SLog;
import util.SLogF;

public class JobSimul {
	protected JobMng g_jm;
	public JobSimul(JobMng jm){
		g_jm=jm;
	}
	
	
	protected int  exec_one(int g_t){
		Job j=g_jm.getCur();
		String s="t:"+g_t+" ";
		if(j==null)	{
			s+="idle";
			SLog.prn(s);
			return 0;
		}
		if(j.exec>0) {
			j.exec--;
			s+="exec "+j.tid;
		} else {
			j.exec=0;
			if(j.opt>0) {
				s+="exec (opt) "+j.tid;
				j.opt--;
			} 
		}
		if(j.exec==0&&j.opt==0) {
			s+=" complete";
			g_jm.removeCur();
		}
		SLog.prn(s);
//		SLogF.prn(s);
		return 1;
	}	
	
	

	

	public int simul_one(int g_t) {
		SLogF.prnc( String.format("%05d ", g_t));
		int dl=dl_check(g_t);
		exec_one(g_t);
		g_t++;
		return dl;
	}
	

	public int simul_end(int g_t){
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




	
}
