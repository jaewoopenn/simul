package TestTaskMode;

import Util.Log;
import Util.TEngine;
import Basic.TaskMng;
import Simul.Analysis;

public class Sch2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,1,0,1,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(20,2,7);
		tm.addHiTask(10,2,3);
		tm.addTask(100,18);
		tm.addTask(100,12);
		tm.addTask(100,10);
		tm.freezeTasks();
		return tm;
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
