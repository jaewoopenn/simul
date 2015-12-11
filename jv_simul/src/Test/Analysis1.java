package Test;

import Util.TEngine;
import Simul.Analysis;
import Simul.Platform;
import Simul.TaskGen;
import Simul.TaskMng;

public class Analysis1 {
//	public static int idx=-1;
	public static int idx=5;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public int test1() // less 1
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		tm.freezeTasks();
		return Analysis.analEDF(tm);
	}

	public int test2() // over 1
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.freezeTasks();
		return Analysis.analEDF(tm);
	}

	public int test3() // exact 1
	{
		TaskMng tm=new TaskMng();
		tm.addTask(2,1);
		tm.addTask(4,1);
		tm.addTask(8,1);
		tm.addTask(16,2);
		tm.freezeTasks();
		return Analysis.analEDF(tm);
	}

	public  int test4() // MC task
	{
		TaskMng tm=new TaskMng();
		tm.addTask(4,2);
		tm.addHiTask(6,1,5);
		tm.freezeTasks();
		return Analysis.analEDF_VD(tm);
	}

	public  int test5()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(13,7);
		tm.addHiTask(12,4,8);
		tm.freezeTasks();
		return Analysis.analEDF_VD(tm);
	}
	
	public  int test6()
	{
		return -1;
	}
	
	public  int test7()
	{
		return -1;
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

	public static void main(String[] args) throws Exception {
		Class c = Analysis1.class;
		Analysis1 m=new Analysis1();
		int[] aret=Analysis1.gret;
		int sz=Analysis1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
