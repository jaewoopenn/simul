package sysEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetFix;

// Task Set MC
public class TS_MC1 {
	public static TaskMng ts1()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,4,2));
		tmp.add(new Task(0,6,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskSetFix tm=new TaskSetFix();
		tm.add(new Task(0,4,3));
		tm.add(new Task(0,8,2,4));
		return tm.getTM();
	}

	public static TaskMng ts3() {
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,8,3));
		tmp.add(new Task(0,12,1,5));
		tmp.add(new Task(0,12,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts4()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,4,1));
		tmp.add(new Task(0,5,1));
		tmp.add(new Task(0,16,2));
		tmp.add(new Task(0,120,19));
		tmp.add(new Task(0,6,1,3));
		return tmp.getTM();
	}
	public static TaskMng ts5() { // not schedulable
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,8,4));
		tmp.add(new Task(0,12,1,5));
		tmp.add(new Task(0,12,1,5));
		return tmp.getTM();
	}
	public static TaskMng ts6() { // not schedulable
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,8,4));
		tmp.add(new Task(0,12,1,2));
		tmp.add(new Task(0,12,1,7));
		return tmp.getTM();
	}

}
