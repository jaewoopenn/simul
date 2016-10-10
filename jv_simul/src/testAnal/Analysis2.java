package testAnal;

import anal.Analysis;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.TEngine;

public class Analysis2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=7;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,8,3));
		tm.add(new Task(0,12,2,8));
		tm.add(new Task(0,40,4,5));
		TaskMng m=tm.freezeTasks();
		return m;
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
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,8,1));
		tm.add(new Task(0,16,2));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public int test3() // exact 1
	{
		TaskMng tm=getTask2();
		return Analysis.anal_EDF(tm);
	}

	public  int test4() // MC task
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,4,2));
		tm.add(new Task(0,6,1,5));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m);
	}

	public  int test5()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,13,7));
		tm.add(new Task(0,12,4,8));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m);
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
