package Simul;

import java.util.Random;

import Util.Log;



public class TaskGenParam {
	private Random g_rand;
	
	public double u_ub;
	public double u_lb;
	public double tu_ub;
	public double tu_lb;
	public double ratio_lb;
	public double ratio_ub;
	public int p_ub;
	public int p_lb;
	
	public TaskGenParam(){
		g_rand=new Random();
		
	}


	public void setUtil(double l, double u) {
		if(l>u ){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
	}


	public void setTUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setTUtil");
		}
		tu_ub=u;
		tu_lb=l;
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

	public Task genTask(int tid){
		int p=g_rand.nextInt(p_ub-p_lb)+p_lb;
		double tu=g_rand.nextDouble()*(tu_ub-tu_lb)+tu_lb;
		int e=(int)(tu*p);
		return new Task(tid,p,e);
	}

	public Task genMCTask(int tid){
		int p=g_rand.nextInt(p_ub-p_lb)+p_lb;
		double tu=g_rand.nextDouble()*(tu_ub-tu_lb)+tu_lb;
		double ratio=g_rand.nextDouble()*(ratio_ub-ratio_lb)+ratio_lb;
		//Log.prn(1,"tu:"+tu+",ratio:"+ratio);
		int h=(int)(tu*p);
		int l=(int)(h*ratio);
		return new Task(tid,p,l,h);
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
		if(util<=u_ub&&util>=u_lb)
			return 1;
		return 0;
	}



	

}
