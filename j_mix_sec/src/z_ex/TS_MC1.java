package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_MC1 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(2,1));
		tmp.add(new Task(10,1,2));
		tmp.add(new Task(100,20,20));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(10,1,5));
		tmp.add(new Task(2,1));
		tmp.add(new Task(100,20,20));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}

	public static TaskMng ts3() {
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(2,1));
		tmp.add(new Task(10,1,2));
		tmp.add(new Task(100,20));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	
	public static TaskMng ts4()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(4,1));
		tmp.add(new Task(5,1));
		tmp.add(new Task(16,2));
		tmp.add(new Task(120,19));
		tmp.add(new Task(6,1,3));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts5() { // not schedulable
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,5));
		tmp.add(new Task(12,1,5));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts6() {   // Hi only test
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,4));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(12,1,7));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}


}
