package Test;

import Util.Log;
import Util.TEngine;
import Basic.TaskGen;
import Basic.TaskMng;
import Exp.Platform;
import Simul.Analysis;

public class Greedy1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public int test1() 
	{
		TaskMng tm=new TaskMng();
		tm.addTask(8,3);
		tm.addHiTask(12,2,8);
		tm.addHiTask(40,4,5);
		tm.freezeTasks();
		return Analysis.anal_EDF_VD(tm); // det:0.855
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
		Class c = Greedy1.class;
		Greedy1 m=new Greedy1();
		int[] aret=Greedy1.gret;
		int sz=Greedy1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
