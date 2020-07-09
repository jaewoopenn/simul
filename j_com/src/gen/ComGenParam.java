package gen;


import util.SLog;
import util.CRange;



public class ComGenParam {
//	private MRand g_rand;
	
	public double u_ub;
	public double u_lb;
	
	public ComGenParam(){
//		g_rand=new MRand();
		
	}


	public void setUtil(CRange r) {
		u_ub=r.getDblU();
		u_lb=r.getDblL();
	}

//	public double genComp() {
//		return g_rand.getDbl(cu_lb,cu_ub);
//	}


	
	public int check(double util) {
		SLog.prn(2," "+util+" "+u_ub+" "+u_lb);
		if(util<=u_ub&&util>=u_lb){
			return 1;
		}
		SLog.prn(2,"f");
		return 0;
	}


	public void prn() {
		SLog.prn(1, u_lb+"");
		SLog.prn(1, u_ub+"");
		
	}
	

}
