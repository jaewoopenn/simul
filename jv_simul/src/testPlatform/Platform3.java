package testPlatform;



import exp.PlatformTM;
import gen.ConfigGen;
import util.Log;
import util.TEngine;

public class Platform3 {
	public static int idx=5;
//	public static int idx=-1;
	public static int log_level=3;
	public int kind=0;
	public int isReal=0;
//	public int prob=1;
	public int prob=4;
//	public int prob=7;
	public PlatformTM getP(){
		return getP1();  // utilization
	}
	
	public PlatformTM getP1() {
		PlatformTM p=getCommmon();
		p.setTSName("util_sim");
		p.setKinds(0);
		p.setStart(85);
		p.setSize(1);
		p.setStep(5);
		return p;
	}
	
	public PlatformTM getCommmon(){
		PlatformTM p=new PlatformTM();
		p.setPath("exp");
		p.setCfg_fn("cfg/cfg_");
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
		} else{
			p.setDuration(100);
			p.setSysNum(1000);
		}
		p.setProb(prob*0.1);
		p.setRS(prob+"");
		return p;
		
	}
	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		PlatformTM p=getP();
		p.writeCfg(eg);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		PlatformTM p=getP();
		p.genTS(true);
		return 1;
	}
	public int test3() 
	{
		PlatformTM p=getP();
//		p.isWrite=false;
		p.setDuration(8000);
		p.setRS("D3");
		p.simul();
		p.setDuration(10000);
		p.setRS("D4");
		p.simul();
		p.setDuration(30000);
		p.setRS("D5");
		p.simul();
		p.setDuration(50000);
		p.setRS("D6");
		p.simul();
		p.setDuration(100000);
		p.setRS("D7");
		p.simul();
		return 1;
	}
	public int test4() 
	{
		return 1;
	}
	public  int test5() 
	{
		PlatformTM p=getP();
		p.setDuration(200000);
		p.simul_one(0,0,55000);
		return 1;
	}
	public  int test6() 
	{
		PlatformTM p=getP();
		p.prnTasks();
		return 0;
	}
	// All in One;
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
		Class c = Platform3.class;
		Platform3 m=new Platform3();
		int[] aret=Platform3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
