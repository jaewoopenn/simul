package Test;
// MC task
// VD

import Util.TEngine;
import Simul.TaskMng;
public class TaskMng2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int total=10;
	public static int gret[]={2,2,-1,-1,-1, -1,-1,-1,-1,-1};
	
	public TaskMng ts1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.freezeTasks();
		return tm;
	}
	
	public int test1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addHiTask(4,1,2);
		return tm.size();
	}
	public int test2() // VD add
	{
		TaskMng tm=ts1();
		tm.setVD(0,2.2);
		tm.setVD(1,3.4);
		tm.prn();
		return tm.size();
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
		int sz=TaskMng2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
}
