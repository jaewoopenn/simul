package testMP;

import anal.AnalEDF_VD;
import exp.PlatformMP;

// anal

import gen.ConfigGen;
import util.Log;
import util.TEngine;

public class PlatformMP2 {
	public static int idx=3;
//	public static int idx=-1;
	public static int log_level=3;
	public int isReal=0;
	
	
	public PlatformMP getCommmon(){
		PlatformMP p=new PlatformMP(2);
		p.setPath("mp");
		p.setCfg_fn("cfg/");
		if(isReal==1){
			p.setDuration(10000);
			p.setSysNum(5000);
		} else{
			p.setDuration(100);
			p.setSysNum(100);
		}
		p.setProb(0.5);
		p.setRS("aa");
		return p;
		
	}

	public PlatformMP getP1() {
		PlatformMP p=getCommmon();
		p.setTSName("util");
		p.setKinds(0);
		
		p.setStart(110);
		p.setStep(10);
//		p.setStart(55);
//		p.setStep(5);
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
		p.genTS();
//		p.genTS(true);
		return 1;
	}
	public int test3() 
	{
		PlatformMP p=getP1();
//		p.isWrite=false;
		p.anal();
		return 1;
	}
	public  int test4() 
	{
		PlatformMP p=getP1();
		p.anal_one(new AnalEDF_VD(),6,1);
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
		Class c = PlatformMP2.class;
		PlatformMP2 m=new PlatformMP2();
		int[] aret=PlatformMP2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}
