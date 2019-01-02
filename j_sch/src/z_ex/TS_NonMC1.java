package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetFile;

// Task Set MC
public class TS_NonMC1 {
	public static TaskMng ts1()	{
		TaskSetFile tm=new TaskSetFile();
		tm.add(new Task(3,1));
		tm.add(new Task(4,2));
		return tm.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskSetFile tm=new TaskSetFile();
		tm.add(new Task(3,1));
		tm.add(new Task(4,3));
		return tm.getTM();
	}
	public static TaskMng ts3()
	{
		TaskSetFile tm=new TaskSetFile();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		tm.add(new Task(5,1));
		return tm.getTM();
	}
	public static TaskMng ts4()
	{
		TaskSetFile tm=new TaskSetFile();
		tm.add(new Task(23,2));
		tm.add(new Task(4,1));
		tm.add(new Task(3,1));
		tm.add(new Task(210,18));
		tm.add(new Task(19,3));
		return tm.getTM();
	}
	public static TaskMng ts5()	{
		TaskSetFile tm=new TaskSetFile();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		return tm.getTM();
	}
}
