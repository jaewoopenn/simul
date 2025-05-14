package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskVec;

// Task Set MC
public class TS_MC2 {
	public static TaskMng ts1()	{ // Hi only test
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(13,1,7));
		return tmp.getTM();
	}

	public static TaskMng ts2()	{ // simple 
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(12,3));
		tmp.add(new Task(20,1,6));
		tmp.add(new Task(17,1,7));
		return tmp.getTM();
	}
	public static TaskMng ts3()	{ // Hi only test
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(10,1));
		tmp.add(new Task(17,2));
		tmp.add(new Task(12,4,5));
		tmp.add(new Task(12,1,7));
		return tmp.getTM();
	}

}
