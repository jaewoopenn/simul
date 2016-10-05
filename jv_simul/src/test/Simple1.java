package test;
import utilSim.Log;
import utilSim.MUtil;
import utilSim.TEngine;

public class Simple1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={1,1,0,0,0, 0,0,0,0,0};
	public int test1() // error config
	{
		int ret=MUtil.facto(5);
		Log.prn(1, "rs:"+ret);
		return 1;
	}
	public int test2() 
	{
		int ret=MUtil.combi(5, 3);
		Log.prn(1, "rs:"+ret);
		ret=MUtil.combi(5, 2);
		Log.prn(1, "rs:"+ret);
		ret=MUtil.combi(4, 2);
		Log.prn(1, "rs:"+ret);
		ret=MUtil.combi(6, 2);
		Log.prn(1, "rs:"+ret);
		ret=MUtil.combi(6, 4);
		Log.prn(1, "rs:"+ret);
		return 1;
	}
	public int test3() 
	{
		Log.prn(1, "hihi");
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
		Class c = Simple1.class;
		Simple1 m=new Simple1();
		int[] aret=Simple1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
