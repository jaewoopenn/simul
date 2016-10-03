package testTaskMode;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import simul.Analysis;
import utilSim.Log;
import utilSim.TEngine;

public class Sch2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,1,0,1,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,20,2,7));
		tm.add(new Task(0,10,2,3));
		tm.add(new Task(0,100,18));
		tm.add(new Task(0,100,12));
		tm.add(new Task(0,100,10));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public int test1() // EDF
	{
		TaskMng tm=getTask1();
		return Analysis.anal_ICG(tm); // det:1.05
	}

	public int test2() // EDF-VD det:0.85
	{
		return 0;
	}
	

	public int test3() // EDF-TM det:0.95
	{
		return 0;
	}
	

	public  int test4() // EDF-VD det:0.95
	{
		return 0;
	}
	

	public  int test5() // EDF-TM det:1.05
	{
		return 0;
	}
	
	
	public  int test6() // EDF-TM det:0.86
	{
		return 0;
	}
	

	public  int test7() // EDF sch/ EDF-TM not sch
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
		Class c = Sch2.class;
		Sch2 m=new Sch2();
		int[] aret=Sch2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
