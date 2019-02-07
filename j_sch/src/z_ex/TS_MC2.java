package z_ex;

import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetEx;

// Task Set MC
public class TS_MC2 {
	public static TaskMng ts1()	{ // Hi only test
		TaskSet tmp=new TaskSet();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(13,1,7));
		TaskSetEx tme=new TaskSetEx(tmp.getVec());
		return tme.getTM();
	}

	public static TaskMng ts2()	{ // simple 
		TaskSet tmp=new TaskSet();
		tmp.add(new Task(8,1));
		tmp.add(new Task(12,3));
		tmp.add(new Task(20,1,6));
		tmp.add(new Task(17,1,7));
		TaskSetEx tme=new TaskSetEx(tmp.getVec());
		return tme.getTM();
	}

}
