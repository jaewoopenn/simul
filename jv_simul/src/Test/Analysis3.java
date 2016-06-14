package Test;

import Util.TEngine;
import Basic.TaskMng;
import Simul.Analysis;

public class Analysis3 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public TaskMng getTask1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(4,1);
		tm.addTask(4,1);
		tm.addHiTask(12,1,5);
		tm.addHiTask(12,1,5);
		tm.freezeTasks();
		return tm;
	}
	public int test1() // EDF-VD
	{
		TaskMng tm=getTask1();
		double dr=Analysis.getDrop_EDF_VD(tm, 0.05);
		return 1;
	}

	public int test2() // EDF-TM det:0.802
	{
		TaskMng tm=getTask1();
		double dr=Analysis.getDrop_EDF_TM_E(tm, 0.05); 
		return 1;
	}

	public int test3() // exact 1
	{
		return 1;
	}

	public  int test4() // MC task
	{
		return 1;
	}

	public  int test5()
	{
		return 1;
	}
	
	public  int test6()
	{
		return 1;
	}
	
	public  int test7()
	{
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
		Class c = Analysis3.class;
		Analysis3 m=new Analysis3();
		int[] aret=Analysis3.gret;
		int sz=Analysis3.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
