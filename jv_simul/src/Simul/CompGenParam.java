package Simul;

import java.util.Random;

import Util.Log;



public class CompGenParam {
	private Random g_rand;
	
	private double u_ub;
	private double u_lb;
	private double ratio_lb;
	private double ratio_ub;
	private int p_ub;
	private int p_lb;
	
	public CompGenParam(){
		g_rand=new Random();
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



	public Comp genComp(int tid) {
		double tu=;
		u=getNext
		// TODO Auto-generated method stub
		return null;
	}



	

}
