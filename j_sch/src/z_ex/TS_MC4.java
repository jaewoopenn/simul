package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_MC4 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1));
		tmp.add(new Task(8,1,4));
		tmp.add(new Task(10,3,4));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	


}
