package testExp;


import exp.Platform;
import simul.ConfigGen;
import utilSim.Log;
import utilSim.TEngine;

public class Platform1 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=3;
	
	private Platform getP1() {
		Platform p=new Platform();
		p.setPath("exp");
		p.setCfg_fn("cfg/cfg");
		p.setStartUtil(50);
		p.setSize(10);
//		p.setStartUtil(80);
//		p.setSize(3);
		p.setDuration(1000);
		p.setProb(0.4);
		return p;
	}
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("num","10");
		Platform p=getP1();
		p.writeCfg(eg);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		Platform p=getP1();
		p.genTS();
		return 1;
	}
	public int test3() 
	{
		Platform p=getP1();
//		p.isWrite=false;
		p.simul();
		return 1;
	}
	public  int test4() 
	{
		Platform p=getP1();
		p.simul_one(0,1,82);
		return 1;
	}
	public  int test5() 
	{
		Platform p=getP1();
		p.simul_vd();
		return 0;
	}
	public  int test6() 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = Platform1.class;
		Platform1 m=new Platform1();
		int[] aret=Platform1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
