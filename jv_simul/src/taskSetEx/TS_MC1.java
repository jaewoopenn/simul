package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;

// Task Set MC
public class TS_MC1 {
	public static TaskMng ts1()	{
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,6,1,5));
		tmp.add(new Task(0,4,2));
		TaskMng tm=tmp.freezeTasks();
		return tm;
	}
	
	public static TaskMng ts2()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,8,2,4));
		tm.add(new Task(0,4,3));
		TaskMng m=tm.freezeTasks();
		return m;
	}

}
