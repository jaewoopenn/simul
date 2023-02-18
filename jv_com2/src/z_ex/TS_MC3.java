package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskVec;

// Task Set MC
public class TS_MC3 {
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(4,2));
		tmp.add(new Task(6,1,5));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1));
		tmp.add(new Task(8,3));
		tmp.add(new Task(12,1,5));
		tmp.add(new Task(12,1,5));
		return tmp.getTM();
	}

	public static TaskMng ts3()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(200,30));
		tmp.add(new Task(300,75));
		tmp.add(new Task(40,3,8));
		tmp.add(new Task(40,3,8));
		tmp.add(new Task(40,3,8));
		tmp.add(new Task(40,3,8));
		return tmp.getTM();
	}
	public static TaskMng ts4()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(197,18));
		tmp.add(new Task(199,5,6));
		tmp.add(new Task(172,7));
		tmp.add(new Task(170,6));
		tmp.add(new Task(139,8));
		tmp.add(new Task(95,2,8));
		tmp.add(new Task(144,5,12));
		tmp.add(new Task(168,12));
		tmp.add(new Task(76,1,2));
		tmp.add(new Task(190,13));
		tmp.add(new Task(144,7));
		tmp.add(new Task(152,3,4));
		return tmp.getTM();
	}
	
}
