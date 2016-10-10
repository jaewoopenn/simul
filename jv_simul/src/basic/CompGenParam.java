package basic;


import utilSim.RUtil;



public class CompGenParam {
	private RUtil g_rand;
	
	public double u_ub;
	public double u_lb;
	public double c_ub;
	public double c_lb;
	public double alpha;
	
	public CompGenParam(){
		g_rand=new RUtil();
		
	}

	public void setUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		u_lb=l;
		u_ub=u;
	}

	public void setCUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setUtil");
		}
		c_lb=l;
		c_ub=u;
	}

	public void setAlpha(double d) {
		alpha=d;
	}



//	public Task genComp(){
//		double tu=g_rand.getDbl(u_lb,u_ub);
//	}


	
	





	

}
