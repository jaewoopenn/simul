package gen;


import task.Task;
import util.MRand;
import util.SLog;



public class TaskGenParam {
	private MRand g_rand;
	
	public double u_ub;
	public double u_lb;
	public double tu_ub;
	public double tu_lb;
	public double ratio_lb;
	public double ratio_ub;
	public double mo_lb;
	public double mo_ub;
	public int p_ub;
	public int p_lb;
	public double prob_HI;
	
	public TaskGenParam(){
		g_rand=new MRand();
		
	}

	public void setUtil(ConfigGen g_cfg) {
		double l=g_cfg.readDbl("u_lb");
		double u=g_cfg.readDbl("u_ub");
		setUtil(l,u);
	}
	
	public void setUtil(double l, double u ) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
//		Log.prn(2, u_lb+" "+u_ub);
	}

	public void setTUtil(ConfigGen g_cfg) {
		double l=g_cfg.readDbl("tu_lb");
		double u=g_cfg.readDbl("tu_ub");
		setTUtil(l,u);
	}

	public void setTUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setTUtil");
		}
		tu_lb=l;
		tu_ub=u;
	}
	public void setRatioLH(ConfigGen g_cfg) {
		setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
	}
	
	public void setRatioLH(double l, double u) {
		if(l>u){
			System.out.println("Error setRatioLH");
		}
		ratio_lb=l;
		ratio_ub=u;
	}
	
	public void setMoLH(ConfigGen g_cfg) {
		setMoLH(g_cfg.readDbl("mo_lb"),g_cfg.readDbl("mo_ub"));
	}
	public void setMoLH(double l, double u) {
		if(l>u){
			System.out.println("Error setMoLH");
		}
		mo_lb=l;
		mo_ub=u;
//		SLog.prn(2, mo_lb+" "+mo_ub);
	}
	
	public void setPeriod(ConfigGen g_cfg) {
		setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
	}
	
	public void setPeriod(int l, int u) {
		if(l>u){
			System.out.println("Error setPeriod");
		}
		p_lb=l;
		p_ub=u;
	}

	public Task genTask(int tid){
//		Log.prn(2, p_lb+" "+p_ub);
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double prob=g_rand.getDbl();
//		SLog.prn(2, prob+" "+prob_HI);
		if(prob<=prob_HI){
			double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
			int h=(int)(tu*p);
			int l=(int)(h*ratio);
			return new Task(p,l,h);
		} else{
			int e=(int)(tu*p);
			return new Task(p,e);
		}
	}
	public Task genTaskIMC(int tid){
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double prob=g_rand.getDbl();
//		SLog.prn(2, prob+" "+prob_HI);
		if(prob<=prob_HI){
			double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
			int h=(int)(tu*p);
			int l=(int)(h*ratio);
			return new Task(p,l,h);
		} else{
			double m_ratio=g_rand.getDbl(mo_lb,mo_ub);
			int l=(int)(tu*p);
			int h=(int)(l*m_ratio);
			return new Task(p,l,h,false);
		}
	}

	
	
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
