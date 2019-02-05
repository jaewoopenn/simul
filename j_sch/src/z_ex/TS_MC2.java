package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetEx;

// Task Set MC
public class TS_MC2 {
	public static TaskMng ts1()	{
		TaskSet tmp=new TaskSet();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(13,1,7));
		TaskSetEx tme=new TaskSetEx(tmp.getVec());
		return tme.getTM();
	}

}
