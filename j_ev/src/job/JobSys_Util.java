package job;



import util.MCal;
import util.SLog;

public class JobSys_Util extends JobSys {
	private JobMng_Util g_jm;
	public JobSys_Util(){
		init();
	}
	@Override
	public void init_in() {
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
//		int real_dl=et-g_dem_base;
		while(true) {
			Job j=g_jm.getCur2();
			if(j==null)
				break;
			if(j.dl<=g_t) {
				g_jm.removeCur2();
			} else {
				break;
			}
		}
		double util=g_jm.getUtil()+(double)(e+o)/dl;
		SLog.prn("test "+MCal.getStr(util)+"<= 1?");
		if(util<=1+MCal.err) {
			Job j=new Job(g_id,et,e,o,v);
			j.rel_dl=dl;
			g_jm.add(j);
			g_val+=v;
			g_id++;
			return true;
		}
		SLog.prn("rejected e:"+(e+o)+",dl:"+dl);
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
