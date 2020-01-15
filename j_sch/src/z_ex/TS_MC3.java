package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_MC3 {
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(4,2));
		tmp.add(new Task(6,1,5));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,3));
		tmp.add(new Task(12,1,5));
		tmp.add(new Task(12,1,5));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}

}
