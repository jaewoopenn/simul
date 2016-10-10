package testAnal;

import anal.Analysis;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.Log;
import utilSim.TEngine;

public class Sch1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=7;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

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
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,20,2,9));
		tm.add(new Task(0,10,2,3));
		tm.add(new Task(0,100,18));
		tm.add(new Task(0,100,12));
		tm.add(new Task(0,100,10));
		TaskMng m=tm.freezeTasks();
		return m;
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
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,90,5,8));
		tm.add(new Task(0,175,1,6));
		tm.add(new Task(0,205,14,15));
		tm.add(new Task(0,172,2,12));
		tm.add(new Task(0,150,7,10));
		tm.add(new Task(0,128,2,8));
		tm.add(new Task(0,94,1,8));
		tm.add(new Task(0,64,1,4));
		tm.add(new Task(0,71,2));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	public TaskMng getTask4() // t1 hutil 0.45
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,10,2,3));
		tm.add(new Task(0,10,1,5));
		tm.add(new Task(0,10,1));
		TaskMng m=tm.freezeTasks();
		return m;
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
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
