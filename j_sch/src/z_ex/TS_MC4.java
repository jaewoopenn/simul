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
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1));
		tmp.add(new Task(8,2,3));
		tmp.add(new Task(10,3,4));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts3()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1));
		tmp.add(new Task(9,2,3));
		tmp.add(new Task(10,2,3));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}

	public static TaskMng getTS(int no) {
		if(no==1)
			return ts1();
		else if(no==2)
			return ts2();
		else if(no==3)
			return ts3();
		else
			return null;
	}


}
