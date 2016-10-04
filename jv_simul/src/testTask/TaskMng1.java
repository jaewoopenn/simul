package testTask;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import taskSetEx.TS_NonMC1;
import utilSim.Log;
import utilSim.TEngine;
public class TaskMng1 {
	public static int idx=3;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		TaskMng m=TS_NonMC1.ts4();
		m.prn();
		return 0;
	}
	public int test2()
	{
		TaskMng m=TS_NonMC1.ts4();
		m.prnLoTasks();
		int tid=m.findDropTask();
		Log.prn(1, ""+tid);
		return 0;
	}
	public  int test3()
	{
		TaskMng m=TS_NonMC1.ts4();
		m.setX(0.3);
		m.prnLoTasks();
		int tid=m.findDropTask();
		Task t=m.getTask(tid);
		Log.prn(1, ""+tid+","+t.getLoUtil()+","+m.getReclaimUtil(tid));
		m.drop(tid);
		tid=m.findDropTask();
		t=m.getTask(tid);
		Log.prn(1, ""+tid+","+t.getLoUtil()+","+m.getReclaimUtil(tid));
		return 0;
	}
	public  int test4()
	{
		return 0;
	}
	public  int test5()
	{
		return 0;
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
		Class c = TaskMng1.class;
		TaskMng1 m=new TaskMng1();
		int[] aret=TaskMng1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
