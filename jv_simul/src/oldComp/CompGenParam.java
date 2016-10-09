package oldComp;


import utilSim.RUtil;



public class CompGenParam {
	private RUtil g_rand=new RUtil();
	
	private int cpus=1;
	private double util_u;
	private double util_l;
	private double tu_u;
	private double tu_l;
	private double ht_lt_u;
	private double ht_lt_l;
	private double ratio_lb;
	private double ratio_ub;
	
	public void set_cpus(int n) {
		cpus=n;
	}

	public void set_util(double l, double u) {
		if(l>u ){
			System.out.println("Error set_util");
		}
		util_l=l;
		util_u=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void set_tu(double l, double u) {
		if(l>u ){
			System.out.println("Error set_lt_lu");
		}
		tu_l=l;
		tu_u=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void set_ht_lt(double l, double u) {
		if(l>u ){
			System.out.println("Error set_ht_lu");
		}
		ht_lt_l=l;
		ht_lt_u=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void set_ratio(double l, double u) {
		if(l>u){
			System.out.println("Error set_ratio");
		}
		ratio_ub=u;
		ratio_lb=l;
		
	}



	public OComp genComp(int tid) {
		double tu=g_rand.getDbl(tu_l, tu_u);
		double ht_lt=g_rand.getDbl(ht_lt_l, ht_lt_u);
		double ratio=g_rand.getDbl(ratio_lb, ratio_ub);
		double lt_lu=tu*ht_lt;
		double ht_lu=tu*(1-ht_lt);
		double ht_hu=ht_lu*ratio;
		return new OComp(tid,lt_lu,ht_lu,ht_hu);
	}

	public int check(double util) {
		if(util<=cpus*util_u&&util>=cpus*util_l){
			return 1;
		}
//		Log.prn(2,"f");
		return 0;
	}

	public double get_util_u() {
		return cpus*util_u;
	}



	

}
