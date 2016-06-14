package TestTaskMode;
import Util.Log;
import Util.TEngine;
import Basic.TaskMng;

public class TaskSet1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={1,1,1,0,0, 0,0,0,0,0};
	public TaskMng getTask1()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(20,2,7);
		tm.addHiTask(10,2,3);
		tm.addTask(100,18);
		tm.addTask(100,12);
		tm.addTask(100,10);
		return tm;
	}
	
	public int test1() // EDF
	{
		TaskMng tm=getTask1();
		tm.sort();
		tm.freezeTasks();
		tm.prn();
		Log.prn(1, "hihi");
		return 1;
	}
	public TaskMng getTask2()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(10,5,3);
		tm.addHiTask(10,4,2);
		tm.addHiTask(10,2,4);
		tm.addHiTask(10,3,6);
		tm.addHiTask(10,1,5);
		return tm;
	}
	
	public int test2() // load
	{
		TaskMng tm=getTask2();
		tm.freezeTasks();
		tm.sort();
		tm.prnHI();
		return 1;
	}
	public TaskMng getTask3()
	{
		TaskMng tm=new TaskMng();
		tm.addHiTask(10,3,4);
		tm.addHiTask(10,3,6);
		tm.addHiTask(10,1,5);
		tm.addTask(10,5);
		tm.addTask(10,6);
		return tm;
	}
	
	public int test3() // load
	{
		TaskMng tm=getTask3();
		tm.freezeTasks();
		tm.sort();
		tm.prnHI();
		tm.prn();
		return 1;
	}
	
	public  int test4() // pick 1
	{
		return 0;
	}
	public  int test5() //
	{
		return 0;
	}
	public  int test6() // 
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
		Class c = TaskSet1.class;
		TaskSet1 m=new TaskSet1();
		int[] aret=TaskSet1.gret;
		int sz=TaskSet1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
