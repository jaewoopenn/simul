package gen;


import task.Task;
import task.TaskMC;
import util.MRand;
import util.SLog;



@SuppressWarnings("unused")
public class TaskGenInd {
	protected MRand g_rand=new MRand();
	
	protected double u_ub;
	protected double u_lb;
	
	protected double tu_ub;
	protected double tu_lb;
	
	protected double ratio_lb;
	protected double ratio_ub;
	
	
	protected int p_ub;
	protected int p_lb;
	
	protected double prob_HI;
	

	
	public void setUtil(double l, double u ) {
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
	public void setProbHI(double d) {
		prob_HI=d;
	}
	
	public Task genTask(int tid){
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double prob=g_rand.getDbl();
		if(prob<=prob_HI){
			double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
			int h=(int)(tu*p);
			int l=(int)(h*ratio);
			return new TaskMC(p,l,h);
		} else{
			int e=(int)(tu*p);
			return new TaskMC(p,e);
		}
	}
	
	
	// check 
	
	public boolean chkTask(Task t) {
		int p=t.period;
		if(p>=p_ub && p<p_lb)
			return false;
		double tu=t.getMaxUtil();
//		SLog.prn(1, tu+","+tu_ub+","+tu_lb);
		if(tu>=tu_ub && tu<tu_lb)
			return false;
//		SLog.prn(1, "OK");
		return true;
	}

	public boolean chkMCTask(Task t) {
		double ratio=(double)(t.c_l)/t.c_h;
		if(ratio>ratio_ub) return false;
		if(ratio<ratio_lb) return false;
		return true;
	}

	public boolean chkUtil(double util) {
//		SLog.prn(2," "+util+" "+u_ub+" "+u_lb);
		if(util>u_ub) return false;
		if(util<u_lb) return false;
		return true;
	}






	

}
