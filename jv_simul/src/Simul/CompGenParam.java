package Simul;

import java.util.Random;

import Util.Log;



public class CompGenParam {
	private Random g_rand;
	
	public double u_ub;
	public double u_lb;
	public double ratio_lb;
	public double ratio_ub;
	public int p_ub;
	public int p_lb;
	public double prob_HI;
	
	public CompGenParam(){
		g_rand=new Random();
	}

	public int getComp(int max)
	{
		return g_rand.nextInt(max);
	}
	public void setUtil(double l, double u) {
		if(l>u ){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}


	public void setRatioLH(double l, double u) {
		if(l>u){
			System.out.println("Error setRatioLH");
		}
		ratio_ub=u;
		ratio_lb=l;
		
	}


	public void setPeriod(int l, int u) {
		if(l>u){
			System.out.println("Error setPeriod");
		}
		p_ub=u;
		p_lb=l;
		
	}

	
	public boolean chkTask(Task t) {
		int p=t.period;
		if(p>=p_ub && p<p_lb)
			return false;
		double tu=(double)(t.c_h)/t.period;
		if(tu>=p_ub && tu<p_lb)
			return false;
		return true;
	}

	public boolean chkMCTask(Task t) {
		double ratio=(double)(t.c_l)/t.c_h;
		if(ratio>=ratio_ub && ratio<ratio_lb)
			return false;
		return true;
	}

	public int check(double util) {
//		Log.prn(2," "+util+" "+u_ub+" "+u_lb);
		if(util<=u_ub&&util>=u_lb){
			return 1;
		}
//		Log.prn(2,"f");
		return 0;
	}

	public Task genComp(int tid) {
		// TODO Auto-generated method stub
		return null;
	}



	

}
