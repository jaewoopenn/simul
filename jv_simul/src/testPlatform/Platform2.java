package testPlatform;


import anal.ConfigGen;
import exp.PlatformTM;
import utill.Log;
import utill.TEngine;


// schedulability 
public class Platform2 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=3;
	public int kind=0;
	public int isReal=0;

	public PlatformTM getP(){
		if(kind==0)
			return getP1();
		else
			return getP2();
	}
	
	public PlatformTM getP1() {
		PlatformTM p=getCommmon();
		p.setTSName("util");
		p.setKinds(0);
		p.setStart(55);
		p.setSize(10);
//		p.setStart(100);
//		p.setSize(1);
		p.setStep(5);
		return p;
	}
	public PlatformTM getP2() {
		PlatformTM p=getCommmon();
		p.setTSName("prob");
		p.setKinds(1);
		p.setStart(5);
		p.setSize(19);
		p.setStep(5);
		return p;
	}

	public PlatformTM getCommmon(){
		PlatformTM p=new PlatformTM();
		p.setPath("exp");
		p.setCfg_fn("cfg/cfg");
		if(isReal==1){
			p.setSysNum(5000);
		} else{
			p.setSysNum(100);
		}
		p.setRS("X");
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
		p.genTS(false);
		return 1;
	}
	public int test3() 
	{
		PlatformTM p=getP();
//		p.isWrite=false;
		p.anal();
//		MUtil.sendMail("ICG anal OK");
		return 1;
	}
	public  int test4() 
	{
		PlatformTM p=getP();
		p.anal_one(0,9,30);
		return 1;
	}
	public  int test5() 
	{
		PlatformTM p=getP();
		p.simul_vd();
		return 0;
	}
	public  int test6() 
	{
		PlatformTM p=getP();
		p.prnTasks();
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
		Class c = Platform2.class;
		Platform2 m=new Platform2();
		int[] aret=Platform2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
