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
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public static TaskMng ts2()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,3));
		TaskMng m=tm.freezeTasks();
		return m;
	}
}
