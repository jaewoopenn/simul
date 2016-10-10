package testComp;


import anal.AnalEDF_AT_S;
import basic.Task;
import basic.TaskMng;
import comp.Comp;
import comp.CompMng;
import utilSim.Log;
import utilSim.TEngine;
import taskSetEx.CompMngEx1;

public class CompMng2 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng3();
		TaskMng tm=cm.getTM();
		double x=AnalEDF_AT_S.computeX(tm);
		cm.setX(x);
		cm.analMaxRes();
		cm.part();
		cm.prn();
		Comp c=cm.getComp(0);
		Log.prn(1, "RU:"+c.getRU());
		cm.prnOff();
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
		Class c = CompMng2.class;
		CompMng2 m=new CompMng2();
		int[] aret=CompMng2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
