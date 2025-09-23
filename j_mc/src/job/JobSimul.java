package job;


import util.SLog;
import util.SLogF;

public class JobSimul {
	protected JobMng g_jm;
	protected int g_t;
	public JobSimul(int n){
		g_t=0;
		g_jm=new JobMng(n);
	}
	
	
	public JobMng getJM() {
		return g_jm;
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
			// removing is processing in ms_check 
			// HI task and add_exec>0 --> process in ms_check
		} else {  // j.exec>1
			out_type=2;
			j.exec-=1;
		}
		s=g_jm.getJobArrow(j,out_type);
		SLogF.prn(s);
	}	
	
	// do ms check after exec
	// check current job exec=0, low task end
	public Job get_ms_job() { 
		Job j=(Job)g_jm.getCur();
		if(j==null)
			return null;
		if(j.exec!=0) 
			return null;
		
		if(j.isHI) {
			return j;
		} else { //HI
			g_jm.removeCur();
			return null;
		}
		
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
			if(j.isHI) {
				if(cur_t<j.vd) 
					break;
				if(SLogF.isON())
					SLogF.save();
				SLog.err("Job Simul: VD miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", vd:"+j.vd+", dl:"+j.dl);
			}else {
				if(cur_t<j.dl) 
					break;
				// cur>=j.dl
				if(j.isDrop()) {
					dm++;
					SLogF.prn("Job Simul: DL miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", vd:"+j.vd+", dl:"+j.dl);
					g_jm.removeCur();
				} else {
					if(SLogF.isON())
						SLogF.save();
					g_jm.prn();
					SLog.err("Job Simul: DL miss at time "+cur_t+": tid:"+j.tid+", left exec:"+(j.exec)+", vd:"+j.vd+", dl:"+j.dl);
				}
				
			}
		}

		return dm;
	}
	
	public boolean is_idle() {
		if(g_jm.isIdle(g_t))
//		if(g_jm.isIdle2())
			return true;
		return false;
	}
	public int get_time() {
		return g_t;
	}

	
}
