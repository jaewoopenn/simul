package taskSetEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;

// Task Set MC
public class TS_MC1 {
	public static TaskMng ts1()	{
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,4,2));
		tmp.add(new Task(0,6,1,5));
		return tmp.freezeTasks();
	}
	
	public static TaskMng ts2()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,4,3));
		tm.add(new Task(0,8,2,4));
		return tm.freezeTasks();
	}

	public static TaskMng ts3() {
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,8,2));
		tmp.add(new Task(0,8,2));
		tmp.add(new Task(0,12,1,5));
		tmp.add(new Task(0,12,1,5));
		return tmp.freezeTasks();
	}
	
	public static TaskMng ts4()	{
		TaskMngPre tmp=new TaskMngPre();
		tmp.add(new Task(0,4,1));
		tmp.add(new Task(0,5,1));
		tmp.add(new Task(0,16,2));
		tmp.add(new Task(0,120,19));
		tmp.add(new Task(0,6,1,3));
		return tmp.freezeTasks();
	}

}
