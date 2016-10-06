package testExp;
import exp.ExpSimul;
import simul.ConfigGen;
import utilSim.Log;
import utilSim.TEngine;

public class ExpSimul1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={1,1,0,6,0,0,0,0,0,0};
	public static int log_level=2;
	public int test1() 
	{
		ConfigGen cfg=new ConfigGen("exp/cfg/cfg_50.txt");
		cfg.readFile();
		//cfg.prn(2);
		ExpSimul eg=new ExpSimul(cfg);
		eg.setDuration(100);
		int size=eg.size();
		int ret=eg.simul();
		Log.prn(2, ret+"/"+size);
		return 1;
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
		Class c = ExpSimul1.class;
		ExpSimul1 m=new ExpSimul1();
		int[] aret=ExpSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
