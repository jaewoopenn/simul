package TestTaskMode;

import Util.Log;
import Util.TEngine;
import Basic.TaskMng;
import Simul.Analysis;

public class Sch1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=7;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

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
		return Analysis.anal_EDF(tm); // det:1.05
	}

	public int test2() // EDF-VD det:0.85
	{
		TaskMng tm=getTask1();
		return Analysis.anal_EDF_VD(tm); 
	}

	public int test3() // EDF-TM det:0.95
	{
		TaskMng tm=getTask1();
		return Analysis.anal_EDF_TM(tm);
	}
	
	public TaskMng getTask2() // t1 hutil 0.45
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(20,2,9);
		tm.addHiTask(10,2,3);
		tm.addTask(100,18);
		tm.addTask(100,12);
		tm.addTask(100,10);
		tm.freezeTasks();
		return tm;
	}

	public  int test4() // EDF-VD det:0.95
	{
		TaskMng tm=getTask2();
		return Analysis.anal_EDF_VD(tm); // det:0.855
	}

	public  int test5() // EDF-TM det:1.05
	{
		TaskMng tm=getTask2();
		return Analysis.anal_EDF_TM(tm);
	}
	
	public  int test6() // EDF-TM det:0.86
	{
		TaskMng tm=getTask2();
		return Analysis.anal_EDF_TM_S(tm);
	}

	public TaskMng getTask3() // t1 hutil 0.45
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(90,5,8);
		tm.addHiTask(175,1,6);
		tm.addHiTask(205,14,15);
		tm.addHiTask(172,2,12);
		tm.addHiTask(150,7,10);
		tm.addHiTask(128,2,8);
		tm.addHiTask(94,1,8);
		tm.addHiTask(64,1,4);
		tm.addTask(71,2);
		tm.freezeTasks();
		return tm;
	}
	public TaskMng getTask4() // t1 hutil 0.45
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(10,2,3);
		tm.addHiTask(10,1,5);
		tm.addTask(10,1);
		tm.freezeTasks();
		return tm;
	}	
	public  int test7() // EDF sch/ EDF-TM not sch
	{
		TaskMng tm=getTask4();
		tm.prn();
		int edf=Analysis.anal_EDF(tm);
		int edf_tm=Analysis.anal_EDF_TM(tm);
		Log.prn(2, "rs "+edf+" "+edf_tm);
		return 1;
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
		Class c = Sch1.class;
		Sch1 m=new Sch1();
		int[] aret=Sch1.gret;
		int sz=Sch1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
