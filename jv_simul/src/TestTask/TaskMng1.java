package TestTask;
import Util.Log;
import Util.TEngine;
import Basic.TaskMng;
public class TaskMng1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=5;
	public static int total=10;
	public static int gret[]={2,3,2,2,67,0,0,0,0,0};
	public int test1()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		return tm.size();
	}
	public int test2()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.addTask(5,1);
		return tm.size();
	}
	public  int test3()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.freezeTasks();
		int[] pt=tm.getPeriods();
		for(int p:pt)
			Log.prn(1, p);
		return pt.length;
	}
	public  int test4()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,1);
		tm.freezeTasks();
		tm.addTask(5,1);
		return tm.size();
	}
	public  int test5()
	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(5,1);
		tm.addTask(7,1);
		tm.freezeTasks();
		tm.prn();
		return (int)(tm.getUtil()*100);
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
		int sz=TaskMng1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
