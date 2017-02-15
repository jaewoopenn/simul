package testAnal;

import anal.AnalEDF_AD_E;
import basic.Task;
import basic.TaskMng;
import basic.TaskSetFix;
import util.Log;
import util.TEngine;

public class Anal1 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=2;

	public int test1() 
	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,4,2));
		tmp.add(new Task(1,6,1,5));
		TaskMng tm=tmp.getTM();
		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		double x=a.getX();
		Log.prn(2, ""+x);
		return -1;
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
		Class c = Anal1.class;
		Anal1 m=new Anal1();
		int[] aret=Anal1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
