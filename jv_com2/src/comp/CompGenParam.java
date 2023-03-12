package comp;

import util.MRand;
import util.SLog;

//import utill.Log;



public class CompGenParam {
	private MRand g_rand;
	
	public double u_ub;
	public double u_lb;
	public double c_ub;
	public double c_lb;
	public double a_ub;
	public double a_lb;
	
	public CompGenParam(){
		g_rand=new MRand();
		
	}

	public void setUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
	}

	public void setCUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		c_lb=l;
		c_ub=u;
	}

	public void setAlpha(double l, double u) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		a_lb=l;
		a_ub=u;
//		Log.prn(1, a_lb+","+a_ub);
	}

	public double getAlpha() {
		if(a_lb==0&a_ub==0) {
			SLog.err("alpha error:"+a_lb+","+a_ub);
		}
		return g_rand.getDbl(a_lb,a_ub);
	}
}
