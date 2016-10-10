package basic;


import utilSim.RUtil;



public class CompGenParam {
	private RUtil g_rand;
	
	public double u_ub;
	public double u_lb;
	
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



//	public Task genComp(){
//		double tu=g_rand.getDbl(u_lb,u_ub);
//	}


	
	





	

}
