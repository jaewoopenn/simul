package TestTaskMode;

import Util.Log;
import Util.TEngine;
import Simul.Analysis;
import Simul.TaskMng;

public class Drop1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=4;
	public static int total=10;
	public static int gret[]={1,1,1,1,0, 0,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(20,2,9);
		tm.addHiTask(10,2,3);
		tm.addTask(100,18);
		tm.addTask(100,12);
		tm.addTask(100,10);
		tm.freezeTasks();
		tm.sort();
		return tm;
	}
	
	public int test1() // EDF
	{
		TaskMng tm=getTask1();
		double d=Analysis.getDrop_EDF_VD(tm, 0.05);
//		Log.prn(1, d+"");
		return 1;
	}

	public int test2() // EDF-VD det:0.85
	{
		TaskMng tm=getTask1();
		
		double d=Analysis.getDrop_EDF_TM_E(tm, 0.05);
//		Log.prn(1, d+"");
		return 1;
	}

	public TaskMng getTask2()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(20,1,4);
		tm.addHiTask(20,1,5);
		tm.addHiTask(10,2,3);
		tm.addTask(100,18);
		tm.addTask(100,12);
		tm.addTask(100,10);
		tm.freezeTasks();
		tm.sort();
		return tm;
	}
	
	public int test3() // EDF-TM det:0.95
	{
		TaskMng tm=getTask2();
		
		double d=Analysis.getDrop_EDF_TM_S(tm, 0.05);
		return 1;
	}
	public TaskMng getTask3()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(40,1,3);
		tm.addHiTask(40,1,5);
		tm.addHiTask(40,1,5);
		tm.addHiTask(40,1,5);
		tm.addHiTask(10,2,3);
		tm.addTask(100,2);
		tm.addTask(100,2);
		tm.addTask(100,1);
		tm.addTask(100,5);
		tm.addTask(100,10);
		tm.addTask(100,10);
		tm.addTask(100,10);
		tm.freezeTasks();
		tm.sort();
		return tm;
	}
	
	public  int test4() // EDF-VD det:0.95
	{
		TaskMng tm=getTask3();
		
		double d=Analysis.getDrop_EDF_TM_E(tm, 0.05);
		return 1;
	}

	public  int test5() // EDF-TM det:1.05
	{
		return 0;
	}
	
	public  int test6() // EDF-TM det:0.86
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
		Class c = Drop1.class;
		Drop1 m=new Drop1();
		int[] aret=Drop1.gret;
		int sz=Drop1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
