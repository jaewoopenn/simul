package sysEx;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetFix;

// Task Set MC
public class TS_MP1 {
	public static TaskMng ts1()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,16,3));
		tmp.add(new Task(0,27,2));
		tmp.add(new Task(0,6,1,2));
		tmp.add(new Task(0,8,1,3));
		tmp.add(new Task(0,14,2,3));
		return tmp.getTM();
	}
	

}
