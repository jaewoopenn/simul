package test;

import util.Log;
import util.RUtil;
import util.TEngine;

public class RUtil1 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1() 
	{
		RUtil r=new RUtil();
		int v=r.getInt(10);
		Log.prn(1, ""+v);
		return 0;
	}
	public int test2() 
	{
		RUtil r=new RUtil();
		double v=r.getDbl();
		Log.prn(1, ""+v);
		return 0;
	}
	public int test3() 
	{
		RUtil r=new RUtil();
		double v=r.getDbl();
		Log.prn(1, ""+v);
		double sucRatio=0.4;
		if (v<sucRatio)
			Log.prn(1,"Suc");
		else
			Log.prn(1,"Fail");
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
		Class c = RUtil1.class;
		RUtil1 m=new RUtil1();
		int[] aret=RUtil1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
