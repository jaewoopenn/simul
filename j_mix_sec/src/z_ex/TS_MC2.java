package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_MC2 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(10,1));
		tmp.add(new Task(12,1,2));
		tmp.add(new Task(100,2,4));
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
		tmp.add(new Task(39, 3, 4));
		tmp.add(new Task(118, 4));
		tmp.add(new Task(48, 2, 7));
		tmp.add(new Task(30, 5));
		tmp.add(new Task(29, 2));
		tmp.add(new Task(123, 3, 10));
		tmp.add(new Task(121, 7, 9));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts5() { 
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(125, 5));
		tmp.add(new Task(138, 7, 8));
		tmp.add(new Task(147, 15));
		tmp.add(new Task(48, 5, 6));
		tmp.add(new Task(137, 3, 12));
		tmp.add(new Task(135, 5, 8));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts6() {   
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(130, 5, 7));
		tmp.add(new Task(139, 3, 12));
		tmp.add(new Task(89, 15));
		tmp.add(new Task(69, 3, 10));
		tmp.add(new Task(135, 1, 2));
		tmp.add(new Task(89, 11));
		tmp.add(new Task(42, 2));
		tmp.add(new Task(54, 4, 6));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts7() {   
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(21, 2));
		tmp.add(new Task(140, 10, 22));
		tmp.add(new Task(60, 5));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}

}
