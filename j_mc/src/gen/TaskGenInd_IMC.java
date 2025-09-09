package gen;


import task.Task;



public class TaskGenInd_IMC extends TaskGenInd{
	
	private double mo_lb;
	private double mo_ub;

	public void setMoLH(double l, double u) {
		if(l>u){
			System.out.println("Error setMoLH");
		}
		mo_lb=l;
		mo_ub=u;
//		SLog.prn(2, mo_lb+" "+mo_ub);
	}
	

	@Override
	public Task genTask(int tid){
		int p=g_rand.getInt(p_lb,p_ub);
		double tu=g_rand.getDbl(tu_lb,tu_ub);
		double prob=g_rand.getDbl();
		if(prob<=prob_HI){
			double ratio=g_rand.getDbl(ratio_lb,ratio_ub);
			int h=(int)(tu*p);
			int l=(int)(h*ratio);
			return new Task(tid, p,l,h,true);
		} else{
			double m_ratio=g_rand.getDbl(mo_lb,mo_ub);
			int l=(int)(tu*p);
			int h=(int)(l*m_ratio);
			return new Task(tid,p,l,h,false);
		}
	}

	






	

}
