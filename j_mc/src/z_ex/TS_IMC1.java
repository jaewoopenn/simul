package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskVec;

// Task Set MC
public class TS_IMC1 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,2,1,false));
		tmp.add(new Task(12,2,5,true));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1,1,false));
		tmp.add(new Task(8,1,3,true));
		tmp.add(new Task(12,3,4,true));
		return tmp.getTM();
	}
	public static TaskMng ts3()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(9,1,3,true));
		tmp.add(new Task(7,2,1,false));
		tmp.add(new Task(13,3,4,true));
		return tmp.getTM();
	}

}
