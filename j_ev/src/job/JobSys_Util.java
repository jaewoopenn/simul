package job;



import util.SLog;

public class JobSys_Util extends JobSys {
	private JobMng_Util g_jm;
	public JobSys_Util(){
		g_jm=new JobMng_Util();
		g_js=new JobSimul(g_jm);
	}
	@Override
	protected void reset() {
		g_dem_base=-1;
		g_jm.reset();
	}
	

	@Override
	protected boolean add_in(int dl, int e, int o, double v) {
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

	public void prn_detail() {
		
	}

	public void prn_ok() {
		g_jm.prn();
		
	}

	
}
