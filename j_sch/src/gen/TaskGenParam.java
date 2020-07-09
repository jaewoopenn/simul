package gen;


import task.Task;
import util.MRand;



public class TaskGenParam {
	private MRand g_rand;
	
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
		g_rand=new MRand();
		
	}

	public int getComp(int max)
	{
		return g_rand.getInt(max);
	}
	public void setUtil(double l, double u) {
		if(l>u){
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
		tu_lb=l;
		tu_ub=u;
	}
	public void setRatioLH(double l, double u) {
		if(l>u){
			System.out.println("Error setRatioLH");
		}
		ratio_lb=l;
		ratio_ub=u;
	}


	public void setPeriod(int l, int u) {
		if(l>u){
			System.out.println("Error setPeriod");
		}
		p_lb=l;
		p_ub=u;
		
	}

	public Task genTask(int tid, boolean isMC){
//		Log.prn(2, p_lb+" "+p_ub);
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double getProb=g_rand.getDbl();
		if(getProb<=prob_HI&&isMC){
			double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
			int h=(int)(tu*p);
			int l=(int)(h*ratio);
			return new Task(p,l,h);
		} else{
			int e=(int)(tu*p);
			return new Task(p,e);
		}
	}


	
	
	public boolean chkTask(Task t) {
		int p=t.period;
		if(p>=p_ub && p<p_lb)
			return false;
		double tu=t.getHiUtil();
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

	public void setProbHI(double d) {
		prob_HI=d;
	}


	public static TaskGenParam getDefault(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setRatioLH(0.25, 1);
		tgp.setUtil(0.45,0.5);
		tgp.setPeriod(300,500);
		tgp.setTUtil(0.01,0.1);
		tgp.setProbHI(0.5);
		return tgp;
		
	}
	

}
