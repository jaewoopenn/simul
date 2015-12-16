package Test;

import Util.Log;
import Util.TEngine;
import Exp.Platform;
import Simul.Analysis;
import Simul.TaskGen;
import Simul.TaskMng;

public class Analysis2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=7;
	public static int total=10;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public int test1() // EDF-VD
	{
		TaskMng tm=new TaskMng();
		tm.addTask(8,3);
		tm.addHiTask(12,2,8);
		tm.addHiTask(40,4,5);
		tm.freezeTasks();
		return Analysis.analEDF_VD(tm); // det:0.855
	}

	public int test2() // EDF-TM det:0.802
	{
		TaskMng tm=new TaskMng();
		tm.addTask(8,3);
		tm.addHiTask(12,2,8);
		tm.addHiTask(40,4,5);
		tm.freezeTasks();
		return Analysis.analEDF_TM(tm);
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
		TaskMng tm=new TaskMng();
		tm.addTask(3,2);
		tm.addTask(4,1);
		tm.freezeTasks();
		int v= Analysis.getRespEDF(tm);
		Log.prn(1, "res:"+v);
		return 0;
	}
	
	public  int test7()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(12,1);
		tm.addHiTask(3,1,2);
		tm.addHiTask(4,1,1);
		tm.freezeTasks();
		int v;
		v= Analysis.getRespEDF(tm);
		Log.prn(1, "EDF res:"+v);
		v= Analysis.getRespEDF_VD(tm);
		Log.prn(1, "VD res:"+v);
		v= Analysis.getRespEDF_TM(tm);
		Log.prn(1, "TM res:"+v);
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

	public static void main(String[] args) throws Exception {
		Class c = Analysis2.class;
		Analysis2 m=new Analysis2();
		int[] aret=Analysis2.gret;
		int sz=Analysis2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
