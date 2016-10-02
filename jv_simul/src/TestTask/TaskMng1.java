package TestTask;
import Util.Log;
import Util.TEngine;
import Basic.Task;
import Basic.TaskMng;
import Basic.TaskMngPre;
public class TaskMng1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=5;
	public static int total=10;
	public static int gret[]={2,3,2,2,67,0,0,0,0,0};
	public int test1()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		TaskMng m=tm.freezeTasks();
		return m.getInfo().getSize();
	}
	public int test2()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,5,1));
		TaskMng m=tm.freezeTasks();
		return m.getInfo().getSize();
	}
	public  int test3()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		TaskMng m=tm.freezeTasks();
		int[] pt=m.getPeriods();
		for(int p:pt)
			Log.prn(1, p);
		return pt.length;
	}
	public  int test4()
	{
		return 0;
	}
	public  int test5()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,5,1));
		tm.add(new Task(0,7,1));
		TaskMng m=tm.freezeTasks();
		m.prn();
		return (int)(m.getInfo().getUtil()*100);
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
