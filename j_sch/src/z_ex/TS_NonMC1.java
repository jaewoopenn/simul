package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_NonMC1 {
	public static TaskMng ts1()	{
		TaskVec tm=new TaskVec();
		tm.add(new Task(3,1));
		tm.add(new Task(4,2));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tm));
		return tme.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tm=new TaskVec();
		tm.add(new Task(3,1));
		tm.add(new Task(4,3));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tm));
		return tme.getTM();
	}
	public static TaskMng ts3()
	{
		TaskVec tm=new TaskVec();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		tm.add(new Task(5,1));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tm));
		return tme.getTM();
	}
	public static TaskMng ts4()
	{
		TaskVec tm=new TaskVec();
		tm.add(new Task(23,2));
		tm.add(new Task(4,1));
		tm.add(new Task(3,1));
		tm.add(new Task(210,18));
		tm.add(new Task(19,3));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tm));
		return tme.getTM();
	}
	public static TaskMng ts5()	{
		TaskVec tm=new TaskVec();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tm));
		return tme.getTM();
	}
}
