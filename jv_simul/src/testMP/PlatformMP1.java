package testMP;

import anal.AnalEDF_AD_E;

// simul

import anal.AnalMP;
import exp.PlatformMP;
import gen.ConfigGen;
import util.Log;
import util.TEngine;

public class PlatformMP1 {
	public static int idx=3;
	public static int log_level=3;
	public int isReal=0;
	private int[] g_r={110,115,120,125,130,135,140,145,150,155};
	
	
	public PlatformMP getCommmon(){
		PlatformMP p=new PlatformMP(2);
		p.setPath("mp");
		p.setCfg_fn("cfg/");
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
		} else{
			p.setDuration(1000);
			p.setSysNum(100);
		}
		p.setProb(0.5);
		p.setRS("aa");
		return p;
		
	}

	
	public PlatformMP getP1() {
		PlatformMP p=getCommmon();
		p.setTSName("util_sim");
		p.setRange(g_r);
		p.setStep(5);
		return p;
	}

	
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		PlatformMP p=getP1();
		p.writeCfg(eg);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		PlatformMP p=getP1();
//		p.genTS();
		p.genTS(new AnalMP());
//		p.genTS(new AnalEDF_VD());
		return 1;
	}
	public int test3() 
	{
		PlatformMP p=getP1();
//		p.isWrite=false;
		p.simul();
		return 1;
	}
	public  int test4() 
	{
		PlatformMP p=getP1();
		p.simul_one(new AnalEDF_AD_E(),1,8,23);
		return 1;
	}
	public  int test5() 
	{
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
		Class c = PlatformMP1.class;
		PlatformMP1 m=new PlatformMP1();
		int[] aret=PlatformMP1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
