package z_ex;

import task.TaskMC;
import task.TaskMng;
import task.TaskVec;

// Task Set MC
public class TS_MC2 {
	public static TaskMng ts1()	{ // Hi only test
		TaskVec tmp=new TaskVec();
		tmp.add(new TaskMC(8,1));
		tmp.add(new TaskMC(8,4));
		tmp.add(new TaskMC(12,1,2));
		tmp.add(new TaskMC(13,1,7));
		return tmp.getTM();
	}

	public static TaskMng ts2()	{ // simple 
		TaskVec tmp=new TaskVec();
		tmp.add(new TaskMC(8,1));
		tmp.add(new TaskMC(12,3));
		tmp.add(new TaskMC(20,1,6));
		tmp.add(new TaskMC(17,1,7));
		return tmp.getTM();
	}
	public static TaskMng ts3()	{ // Hi only test
		TaskVec tmp=new TaskVec();
		tmp.add(new TaskMC(10,1));
		tmp.add(new TaskMC(17,2));
		tmp.add(new TaskMC(12,4,5));
		tmp.add(new TaskMC(12,1,7));
		return tmp.getTM();
	}

}
