package testAnal;

import comp.CompFile;
import comp.CompMng;
import anal.AnalComp;
import anal.AnalEDF_AT_S;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import taskSetEx.CompMngEx1;
import utilSim.Log;
import utilSim.TEngine;

public class Anal2 {
	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;

	public int test1() 
	{
		CompMng cm=CompMngEx1.getCompMng2();
//		cm.setAlpha(0, 0.25);
		cm.setAlpha(0, 0);
		AnalComp a=new AnalComp(cm);
		a.computeX();
		a.part();
		cm.prn();
		a.anal();
//		cm.prn();
//		Log.prn(2, "x:"+x);
		return -1;
	}

	public int test2() 
	{
		CompMng cm=CompFile.loadFile("com/ts/taskset_util_70_5");
		cm.setAlpha(0, 0);
		AnalComp a=new AnalComp(cm);
		a.computeX();
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
		Class c = Anal2.class;
		Anal2 m=new Anal2();
		int[] aret=Anal2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
