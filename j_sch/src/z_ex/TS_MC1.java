package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetFile;

// Task Set MC
public class TS_MC1 {
	public static TaskMng ts1()	{
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,4,2));
		tmp.add(new Task(1,6,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,4,3));
		tmp.add(new Task(1,8,2,4));
		return tmp.getTM();
	}

	public static TaskMng ts3() {
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(1,8,3));
		tmp.add(new Task(2,12,1,5));
		tmp.add(new Task(3,12,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts4()	{
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,4,1));
		tmp.add(new Task(1,5,1));
		tmp.add(new Task(2,16,2));
		tmp.add(new Task(3,120,19));
		tmp.add(new Task(4,6,1,3));
		return tmp.getTM();
	}
	public static TaskMng ts5() { // not schedulable
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(1,8,4));
		tmp.add(new Task(2,12,1,5));
		tmp.add(new Task(3,12,1,5));
		return tmp.getTM();
	}
	public static TaskMng ts6() { 
		TaskSetFile tmp=new TaskSetFile();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(1,8,4));
		tmp.add(new Task(2,12,1,2));
		tmp.add(new Task(3,12,1,7));
		return tmp.getTM();
	}

}
