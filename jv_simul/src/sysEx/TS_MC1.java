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
	public static TaskMng ts6() { 
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,8,4));
		tmp.add(new Task(0,12,1,2));
		tmp.add(new Task(0,12,1,7));
		return tmp.getTM();
	}
	public static TaskMng ts7() { 
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,236, 20, 23));
		tmp.add(new Task(0,116, 8, 10));
		tmp.add(new Task(0,94, 3, 8));
		tmp.add(new Task(0,159, 12, 13));
		tmp.add(new Task(0,209, 6, 16));
		tmp.add(new Task(0,200, 14, 15));
		tmp.add(new Task(0,200, 6, 15));
		tmp.add(new Task(0,255, 15, 19));
		tmp.add(new Task(0,59, 4));
		tmp.add(new Task(0,222, 5, 15));
		tmp.add(new Task(0,151, 2, 10));
		tmp.add(new Task(0,169, 3, 11));
		tmp.add(new Task(0,188, 3, 12));
		tmp.add(new Task(0,148, 9));
		tmp.add(new Task(0,234, 14));
		tmp.add(new Task(0,76, 3, 4));
		tmp.add(new Task(0,288, 15));
		return tmp.getTM();
	}

}
