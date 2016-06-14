package Simul;


import Util.Log;
import Util.RUtil;



public class TaskGenParam {
	private RUtil g_rand;
	
	public double u_ub;
	public double u_lb;
	public double tu_ub;
	public double tu_lb;
	public double ratio_lb;
	public double ratio_ub;
	public int p_ub;
	public int p_lb;
	public double prob_HI;
	
	public TaskGenParam(){
		g_rand=new RUtil();
		
	}

	public int getComp(int max)
	{
		return g_rand.getInt(max);
	}
	public void setUtil(double l, double u) {
		if(l>u ){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
//		Log.prn(2, u_lb+" "+u_ub);
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
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		int e=(int)(tu*p);
		return new Task(tid,p,e);
	}

	public Task genMCTask(int tid){
		double getProb=g_rand.getDbl();
		if (getProb>prob_HI) 
			return genTask(tid);
		int p=g_rand.getInt(p_lb, p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
		//Log.prn(1,"tu:"+tu+",ratio:"+ratio);
		int h=(int)(tu*p);
		int l=(int)(h*ratio);
		if(l==0) l=1;
		if(h==0) h=1;
		return new Task(tid,p,l,h);
	}

	public Task genMCTask2(int tid){
		double getProb=g_rand.getDbl();
		if (getProb>prob_HI) 
			return genTask(tid);
		int p=g_rand.getInt(p_lb, p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
		//Log.prn(1,"tu:"+tu+",ratio:"+ratio);
		int h=(int)(tu*p);
		int l=(int)(h*ratio);
		if(l==0) l=1;
		if(h==0) h=1;
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
//		Log.prn(2," "+util+" "+u_ub+" "+u_lb);
		if(util<=u_ub&&util>=u_lb){
			return 1;
		}
//		Log.prn(2,"f");
		return 0;
	}



	

}
