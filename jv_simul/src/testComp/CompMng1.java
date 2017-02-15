package testComp;


import anal.AnalEDF_AD_E;
import basic.Task;
import basic.TaskMng;
import comp.CompMng;
import utill.Log;
import utill.TEngine;
import sysEx.CompMngEx1;

public class CompMng1 {
	public static int idx=4;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng1();
		cm.prn();
		return 0;
	}
	public int test2() 
	{
		CompMng cm=CompMngEx1.getCompMng2();
		cm.prn();
		return 0;
	}
	public int test3() 
	{
		CompMng cm=CompMngEx1.getCompMng1();
		cm.prn();
		TaskMng tm=cm.getTM();
		tm.prn();
		Task t=tm.getTask(1);
		Log.prn(1,"CID:"+t.getComp());
		return 0;
	}
	public  int test4() 
	{
		CompMng cm=CompMngEx1.getCompMng2();
		TaskMng tm=cm.getTM();
		tm.prn();
		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		double x=a.getX();
		tm.setX(x);
		cm.prn();
//		Log.prn(2, "x:"+x);
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
		Class c = CompMng1.class;
		CompMng1 m=new CompMng1();
		int[] aret=CompMng1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
