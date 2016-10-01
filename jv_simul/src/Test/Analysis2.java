package Test;

import Util.Log;
import Util.TEngine;
import Basic.TaskMng;
import Simul.Analysis;

public class Analysis2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=7;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(8,3);
		tm.addHiTask(12,2,8);
		tm.addHiTask(40,4,5);
		tm.freezeTasks();
		return tm;
	}
	
	public int test1() // EDF-VD
	{
		TaskMng tm=getTask1();
		return Analysis.anal_EDF_VD(tm); // det:0.855
	}

	public int test2() // EDF-TM det:0.802
	{
		TaskMng tm=getTask1();
		return Analysis.anal_EDF_TM(tm);
	}

	public TaskMng getTask2()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(4,1);
		tm.addTask(8,1);
		tm.addTask(16,2);
		tm.freezeTasks();
		return tm;
	}
	
	public int test3() // exact 1
	{
		TaskMng tm=getTask2();
		return Analysis.anal_EDF(tm);
	}

	public  int test4() // MC task
	{
		TaskMng tm=new TaskMng();
		tm.addTask(4,2);
		tm.addHiTask(6,1,5);
		tm.freezeTasks();
		return Analysis.anal_EDF_VD(tm);
	}

	public  int test5()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(13,7);
		tm.addHiTask(12,4,8);
		tm.freezeTasks();
		return Analysis.anal_EDF_VD(tm);
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
		Class c = Analysis2.class;
		Analysis2 m=new Analysis2();
		int[] aret=Analysis2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
