package job;



import util.SLog;

public class JobSys_FIFO extends JobSys {
	private JobMng_FIFO g_jm;
	public JobSys_FIFO(){
		g_jm=new JobMng_FIFO();
		g_js=new JobSimul(g_jm);
//		reset();
	}
	//////////////////
	/// Simul related 
	public void exec(int len) {
		int et=g_t+len;
		while(g_t<et) {
			int rs=g_js.simul_one(g_t);
			if(rs==0) {
				g_dem_base=-1;
				g_jm.reset();
			}
			g_t++;
		}
	}

	
	public boolean add(int dl, int e, int o, double v) {
        if(g_dem_base==-1) {
        	g_dem_base=g_t;
        }
		int et=g_t+dl;
		int real_dl=et-g_dem_base;
		int dem=g_jm.getDemand()+e+o;
		SLog.prn("test "+dem+"<= "+real_dl+"?");
		if(dem<=real_dl) {
			Job j=new Job(g_id,et,e,o,v);
			g_jm.add(j);
			g_val+=v;
			g_id++;
			return true;
		}
		SLog.prn("rejected e:"+e+",dl:"+dl);
		return false;
		
	
	}
	
	//////////////////
	/// print related 

	public void prn_dbf() {
		
	}

	public void prn_ok() {
		g_jm.prn();
		
	}

	
}
