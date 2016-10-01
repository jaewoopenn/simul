package TestExp;


import Exp.ExpSimul;
import Simul.ConfigGen;
import Simul.SimGen;
import Util.Log;
import Util.TEngine;

public class Combined1 {
	public static int log_level=3;
//	public static int idx=-1;
	public static int idx=4;
	public static int st=85;
	public static int num=6;
	public static int durations=10000;
	public static int gret[]={1,1,1,1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("subfix", "exp/ts");
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("num","100");
		eg.genRange("exp/cfg/cfg",st,5,num);
		Log.prn(3, "cfg");
		return 1;
	}
	public int test2() 
	{
		for(int i=0;i<num;i++){
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			eg.gen();
		}
		Log.prn(3, "task");
		return 1;
	}
	public int test3() 
	{
		for(int i=0;i<num;i++){
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul(cfg);
			int size=eg.size();
			int ret=eg.load(durations);
			Log.prn(3, (st+5+i*5)+":"+ret+"/"+size+","+(ret*1.0/size));
		}
		return 1;
	}
	public  int test4() 
	{
		test1();
		test2();
		test3();
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
		Class c = Combined1.class;
		Combined1 m=new Combined1();
		int[] aret=Combined1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
