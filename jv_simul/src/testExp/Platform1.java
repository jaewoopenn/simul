package testExp;


import exp.Platform;
import simul.ConfigGen;
import utilSim.Log;
import utilSim.TEngine;

public class Platform1 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=2;

	private Platform getCommmon(){
		Platform p=new Platform();
		p.setPath("exp");
		p.setCfg_fn("cfg/cfg");
//		p.setDuration(10000);
		p.setDuration(1000);
		p.setSysNum(100);
//		p.setProb(0.1);
		p.setProb(0.4);
//		p.setProb(0.7);
//		p.setProb(1.0);
//		p.setRS("1");
		p.setRS("4");
//		p.setRS("7");
		return p;
		
	}
	
	private Platform getP1() {
		Platform p=getCommmon();
		p.setTSName("util");
		p.setKinds(0);
		p.setStart(50);
		p.setSize(10);
		p.setStep(5);
		return p;
	}
	private Platform getP2() {
		Platform p=getCommmon();
		p.setTSName("prob_hi");
		p.setKinds(1);
		p.setStart(10);
		p.setSize(9);
		p.setStep(10);
		return p;
	}

	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		Platform p=getP2();
		p.writeCfg(eg);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		Platform p=getP2();
		p.genTS();
		return 1;
	}
	public int test3() 
	{
		Platform p=getP2();
//		p.isWrite=false;
		p.simul();
		return 1;
	}
	public  int test4() 
	{
		Platform p=getP1();
//		for(int i=0;i<1000;i++)
//			p.simul_one(0,2,i);
		p.simul_one(0,0,30);
//		p.simul_one(1,5,999);
//		p.simul_one(0,5,8);
//		p.simul_one(0,9,8);
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
