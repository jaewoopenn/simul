package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetEx;

// Task Set MC
public class TS_NonMC1 {
	public static TaskMng ts1()	{
		TaskSet tm=new TaskSet();
		tm.add(new Task(3,1));
		tm.add(new Task(4,2));
		TaskSetEx tme=new TaskSetEx(tm.getVec());
		return tme.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskSet tm=new TaskSet();
		tm.add(new Task(3,1));
		tm.add(new Task(4,3));
		TaskSetEx tme=new TaskSetEx(tm.getVec());
		return tme.getTM();
	}
	public static TaskMng ts3()
	{
		TaskSet tm=new TaskSet();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		tm.add(new Task(5,1));
		TaskSetEx tme=new TaskSetEx(tm.getVec());
		return tme.getTM();
	}
	public static TaskMng ts4()
	{
		TaskSet tm=new TaskSet();
		tm.add(new Task(23,2));
		tm.add(new Task(4,1));
		tm.add(new Task(3,1));
		tm.add(new Task(210,18));
		tm.add(new Task(19,3));
		TaskSetEx tme=new TaskSetEx(tm.getVec());
		return tme.getTM();
	}
	public static TaskMng ts5()	{
		TaskSet tm=new TaskSet();
		tm.add(new Task(3,1));
		tm.add(new Task(4,1));
		TaskSetEx tme=new TaskSetEx(tm.getVec());
		return tme.getTM();
	}
}
