package testExp;
import comp.CompMng;
import exp.ExpSimulCom;
import gen.ConfigGen;
import util.SLog;
import util.SEngineT;

public class ExpSimul2 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("fc/cfg/cfg_util_95.txt");
		cfg.readFile();
		//cfg.prn(2);
		ExpSimulCom eg=new ExpSimulCom(cfg);
		CompMng cm=eg.loadCM(0);
		int ret=eg.analComp(cm,0);
		SLog.prn(1, ""+ret);
		return 0;
	}
	public int test2() 
	{
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
		Class c = ExpSimul2.class;
		ExpSimul2 m=new ExpSimul2();
		int[] aret=ExpSimul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
