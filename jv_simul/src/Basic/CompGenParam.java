package Basic;

import java.util.Random;

import Util.Log;
import Util.RUtil;



public class CompGenParam {
	private RUtil g_rand=new RUtil();
	
	private double lt_lu_u;
	private double lt_lu_l;
	private double ht_lu_u;
	private double ht_lu_l;
	private double ratio_lb;
	private double ratio_ub;
	

	public void set_lt_lu(double l, double u) {
		if(l>u ){
			System.out.println("Error setUtil");
		}
		lt_lu_l=l;
		lt_lu_u=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void set_ht_lu(double l, double u) {
		if(l>u ){
			System.out.println("Error setUtil");
		}
		ht_lu_l=l;
		ht_lu_u=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void set_ratio(double l, double u) {
		if(l>u){
			System.out.println("Error setRatioLH");
		}
		ratio_ub=u;
		ratio_lb=l;
		
	}



	public Comp genComp(int tid) {
		double lt_lu=g_rand.getDbl(lt_lu_l, lt_lu_u);
		double ht_lu=g_rand.getDbl(ht_lu_l, ht_lu_u);
		double ratio=g_rand.getDbl(ratio_lb, ratio_ub);
		return new Comp(tid,tu,tu,tu);
	}



	

}
