package TestExp;


import Simul.ConfigGen;
import Util.TEngine;

public class Combined1 {
	public static int log_level=1;
	public static int idx=-1;
//	public static int idx=1;
	public static int gret[]={1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "exp/ts");
		eg.setParam("num","10");
		eg.setParam("u_lb", "0.45");
		eg.setParam("u_ub", "0.50");
		eg.setParam("mod", "50");
		eg.write("exp/cfg/cfg_50.txt");
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
		Class c = Combined1.class;
		Combined1 m=new Combined1();
		int[] aret=Combined1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
