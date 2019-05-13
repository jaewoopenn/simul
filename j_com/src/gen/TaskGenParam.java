package gen;


import basic.Task;
import util.RUtil;
import util.Ranger;



public class TaskGenParam {
	private RUtil g_rand;
	
	public double u_ub;
	public double u_lb;
	public double tu_ub;
	public double tu_lb;
	public int p_ub;
	public int p_lb;
	
	public TaskGenParam(){
		g_rand=new RUtil();
		
	}

	public int getComp(int max)
	{
		return g_rand.getInt(max);
	}
	public void setUtil(Ranger r) {
		u_lb=r.getDblL();
		u_ub=r.getDblU();
//		Log.prn(2, u_lb+" "+u_ub);
	}


	public void setTUtil(Ranger r) {
		tu_ub=r.getDblL();
		tu_lb=r.getDblU();
	}


	public void setPeriod(Ranger r) {
		p_ub=r.getIntU();
		p_lb=r.getIntL();
		
	}

	public Task genTask(int tid, boolean isMC){
//		Log.prn(2, p_lb+" "+p_ub);
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		int e=(int)(tu*p);
		return new Task(p,e);
	}


	
	
	public boolean chkTask(Task t) {
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
		tgp.setUtil(new Ranger(0.45,0.5));
		tgp.setPeriod(new Ranger(300,500));
		tgp.setTUtil(new Ranger(0.01,0.1));
		return tgp;
		
	}
	

}
