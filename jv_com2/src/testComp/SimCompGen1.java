package testComp;


import gen.ConfigGen;
import comp.SimCompGen;
import util.SEngineT;

public class SimCompGen1 {
//	public static int idx=1;
	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("fc/cfg/cfg_util_95.txt");
		cfg.readFile();
		SimCompGen eg=new SimCompGen(cfg);
		eg.gen(false);
		
		return 0;
	}
	public int test2() 
	{
//		ConfigGen cfg=new ConfigGen("fc/cfg/cfg_util_95.txt");
////		Anal an=new AnalEDF_AD_E();
//		cfg.readFile();
//		ExpSimulCom eg=new ExpSimulCom(cfg);
//		CompMng cm=eg.loadCM(1);
//		cm.setAlpha(0,0.3);
//		int ret=eg.analComp(cm,1);
//		SLog.prn(3, ret);
		
		return 0;
	}
	public int test3() 
	{
		return 0;
	}
	public  int test4() 
	{
		return 0;
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
		Class c = SimCompGen1.class;
		SimCompGen1 m=new SimCompGen1();
		int[] aret=SimCompGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
