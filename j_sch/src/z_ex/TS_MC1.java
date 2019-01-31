package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetEx;

// Task Set MC
public class TS_MC1 {
	public static TaskMng ts1()	{
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(4,2));
		tmp.add(new Task(6,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(4,3));
		tmp.add(new Task(8,2,4));
		return tmp.getTM();
	}

	public static TaskMng ts3() {
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,3));
		tmp.add(new Task(12,1,5));
		tmp.add(new Task(12,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts4()	{
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(4,1));
		tmp.add(new Task(5,1));
		tmp.add(new Task(16,2));
		tmp.add(new Task(120,19));
		tmp.add(new Task(6,1,3));
		return tmp.getTM();
	}
	public static TaskMng ts5() { // not schedulable
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,5));
		tmp.add(new Task(12,1,5));
		return tmp.getTM();
	}
	public static TaskMng ts6() { 
		TaskSetEx tmp=new TaskSetEx();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(12,1,7));
		return tmp.getTM();
	}

}
