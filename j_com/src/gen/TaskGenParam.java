package gen;


import util.MRand;
import util.SLog;
import task.Task;
import util.CRange;



public class TaskGenParam {
	private MRand g_rand;
	
	public double u_ub;
	public double u_lb;
	public double tu_ub;
	public double tu_lb;
	public int p_ub;
	public int p_lb;
	
	public TaskGenParam(){
		g_rand=new MRand();
		
	}

	public int getComp(int max)
	{
		return g_rand.getInt(max);
	}
	public void setUtil(CRange r) {
		u_lb=r.getDblL();
		u_ub=r.getDblU();
//		Log.prn(2, u_lb+" "+u_ub);
	}


	public void setTUtil(CRange r) {
		tu_lb=r.getDblL();
		tu_ub=r.getDblU();
	}


	public void setPeriod(CRange r) {
		p_ub=r.getIntU();
		p_lb=r.getIntL();
		
	}

	public Task genTask(int tid, boolean isMC){
//		Log.prn(2, p_lb+" "+p_ub);
		int p=0,e=0;
		double tu=0;
		p=g_rand.getInt(p_lb,p_ub);
		double r=g_rand.getDbl();
		double lb1=0.002;
		double ub1=0.05;
		double lb2=0.05;
		double ub2=0.1;
		if(tu_lb==-1) { // light
			if(r<=8.0/9) {
				tu=g_rand.getDbl(lb1,ub1);
//				SLog.prn(3, "l");
			} else {
				tu=g_rand.getDbl(lb2,ub2);
			}
//			SLog.prn(3, tu+"-1 "+r);
		} else if(tu_lb==-2) { // medium
			if(r<=6.0/9) {
				tu=g_rand.getDbl(lb1,ub1);
			} else {
				tu=g_rand.getDbl(lb2,ub2);
			}
//			SLog.prn(3, tu+"-2 "+r);
		} else if(tu_lb==-3) { // heavy
			if(r<=4.0/9) {
				tu=g_rand.getDbl(lb1,ub1);
			} else {
				tu=g_rand.getDbl(lb2,ub2);
			}
//			SLog.prn(3, tu+"-3 "+r);
		} else {
			tu=g_rand.getDbl(tu_lb,tu_ub);
//			SLog.prn(3, tu+"___");
		}
		e=(int)(tu*p);
		int d=(int) (p*g_rand.getDbl(0.8, 1)+0.5);
		return new Task(p,e,d);
	}


	
	
	public boolean chkTask(Task t) {
		if(t.exec==0)
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


	public static TaskGenParam getDefault(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(new CRange(0.45,0.5));
		tgp.setPeriod(new CRange(300,500));
		tgp.setTUtil(new CRange(0.01,0.1));
		return tgp;
		
	}

	public void prn() {
		SLog.prn(1, u_lb+"");
		SLog.prn(1, u_ub+"");
		
	}
	

}
