package testPlatform;


import anal.ConfigGen;
import exp.PlatformCom;
import utill.Log;
import utill.TEngine;


// schedulability 
public class Platform4 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=3;
	public PlatformCom getP(){
		return getP1();
//		return getP2();
	}
	
	public PlatformCom getP1() {
		PlatformCom p=getCommmon();
		p.setTSName("util_sim");
		p.setKinds(0);
		p.setStart(55);
		p.setSize(10);
//		p.setStart(100);
//		p.setSize(1);
		p.setStep(5);
		return p;
	}
	public PlatformCom getP2() {
		PlatformCom p=getCommmon();
		p.setTSName("prob_sim");
		p.setKinds(1);
		p.setStart(5);
		p.setSize(19);
		p.setStep(5);
		return p;
	}

	public PlatformCom getCommmon(){
		PlatformCom p=new PlatformCom();
		p.setPath("com");
		p.setCfg_fn("cfg/cfg");
		p.setAlpha(0.3,0.6);
		p.setDuration(10000);
//		p.setDuration(1000);
//		p.setDuration(100);
		
//		p.setSysNum(5000);
		p.setSysNum(1000);
//		p.setSysNum(300);
//		p.setSysNum(100);
//		p.setSysNum(10);
//		p.setSysNum(1);
		
//		p.setRS("1");
//		p.setProb(0.1);
		p.setProb(0.4);
		p.setRS("4");
//		p.setProb(0.7);
//		p.setRS("7");

		return p;
		
	}
	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("c_lb","0.1");
		eg.setParam("c_lb","0.05");
//		eg.setParam("c_ub","0.2");
		eg.setParam("u_lb", "0.85");
		eg.setParam("u_ub", "0.90");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		PlatformCom p=getP();
		p.writeComCfg(eg);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		PlatformCom p=getP();
		p.genCom(true);
		return 1;
	}
	public int test3() 
	{
		PlatformCom p=getP();
		p.setAlpha(0.3,0.6);
//		p.isWrite=false;
		p.simulCom();
		return 1;
	}
	public  int test4() 
	{
		int set=9;
		int no=72;
		for(int i=0;i<1000;i++){
			no=i;
			PlatformCom p=getP();
			p.setAlpha(0.1,0.3);
			Log.prn(3, no+"");
//			p.simulCom_one(0,set,no);
			p.simulCom_one(1,set,no);
		}
		return 1;
	}
	public  int test5() 
	{
		int set=9;
		int no=192;
		PlatformCom p=getP();
		p.setAlpha(0.1,0.3);
		Log.prn(3, no+"");
		p.simulCom_one(1,set,no);
		return 1;
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
		Class c = Platform4.class;
		Platform4 m=new Platform4();
		int[] aret=Platform4.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
