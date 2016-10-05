package testSim;
import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.Log;
import utilSim.TEngine;

public class TaskSet1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={1,1,1,0,0, 0,0,0,0,0};
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
		tm.sort();
		tm.prn();
		Log.prn(1, "hihi");
		return 1;
	}
	public TaskMng getTask2()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,10,5,3));
		tm.add(new Task(0,10,4,2));
		tm.add(new Task(0,10,2,4));
		tm.add(new Task(0,10,3,6));
		tm.add(new Task(0,10,1,5));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public int test2() // load
	{
		TaskMng tm=getTask2();
		tm.sort();
		tm.prnHI();
		return 1;
	}
	public TaskMng getTask3()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,10,3,4));
		tm.add(new Task(1,10,3,6));
		tm.add(new Task(2,10,1,5));
		tm.add(new Task(3,10,5));
		tm.add(new Task(4,10,6));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public int test3() // load
	{
		TaskMng tm=getTask3();
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
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
