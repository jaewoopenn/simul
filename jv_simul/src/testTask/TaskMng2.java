package testTask;
// MC task
// VD

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.TEngine;
public class TaskMng2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={2,2,-1,-1,-1, -1,-1,-1,-1,-1};
	
	public TaskMng ts1()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public int test1()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1,2));
		TaskMng m=tm.freezeTasks();
		return m.getInfo().getSize();
	}
	public int test2() // VD add
	{
		TaskMng tm=ts1();
		tm.setVD(0,2.2);
		tm.setVD(1,3.4);
		tm.prn();
		return tm.getInfo().getSize();
	}
	public  int test3()
	{
		return -1;
	}
	public  int test4()
	{
		return -1;
	}
	public  int test5()
	{
		return -1;
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
		return -1;
	}
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskMng2.class;
		TaskMng2 m=new TaskMng2();
		int[] aret=TaskMng2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
