package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;

// Task Set MC
public class TS_NonMC1 {
	public static TaskMng ts1()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,2));
		return tm.freezeTasks();
	}
	
	public static TaskMng ts2()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,3));
		return tm.freezeTasks();
	}
	public static TaskMng ts3()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,5,1));
		return tm.freezeTasks();
	}
	public static TaskMng ts4()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,23,2));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,210,18));
		tm.add(new Task(0,19,3));
		return tm.freezeTasks();
	}
}
