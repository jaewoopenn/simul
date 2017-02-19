package testMP;

// simul

import anal.AnalEDF_VD;
import gen.ConfigGen;
import part.PlatformMP;
import simul.TaskSimul_EDF_VD;
import util.Log;
import util.TEngine;

public class PlatformMP1 {
	public static int idx=4;
//	public static int idx=-1;
	public static int log_level=3;
	public int isReal=0;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	
	public PlatformMP getCommmon(){
		PlatformMP p=new PlatformMP(2);
		p.setPath("mp");
		p.setCfg_fn("cfg/");
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
		} else{
			p.setDuration(100);
			p.setSysNum(10);
		}
		p.setProb(0.5);
		p.setRS("aa");
		return p;
		
	}

	
	public PlatformMP getP1() {
		PlatformMP p=getCommmon();
		p.setTSName("util_sim");
		p.setKinds(0);
//		p.setStart(110);
//		p.setStep(10);
		p.setStart(55);
		p.setStep(5);
		p.setSize(10);
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
		p.genTS(false);
//		p.genTS(true);
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
		p.simul_one(new AnalEDF_VD(),new TaskSimul_EDF_VD(),0,0);
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

}
