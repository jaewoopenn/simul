package testExp;
import gen.ConfigGen;
//import utill.Log;
import util.SEngineT;

public class ConfigWrite2 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		ConfigGen eg=ConfigGen.getCfg();
		eg.setParam("subfix", "com/ts");
		eg.setParam("num","10");
		eg.setParam("u_lb", "0.45");
		eg.setParam("u_ub", "0.50");
		eg.setParam("c_lb", "0.1");
		eg.setParam("c_ub", "0.3");
		eg.setParam("a_lb","0.2");
		eg.setParam("a_ub","0.3");
		eg.setParam("mod", "50");
		eg.write("file/cfg_50.txt");
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
		Class c = ConfigWrite2.class;
		ConfigWrite2 m=new ConfigWrite2();
		int[] aret=ConfigWrite2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
