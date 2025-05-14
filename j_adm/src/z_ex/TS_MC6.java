package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskVec;

// Task Set MC AMC
public class TS_MC6 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,1));
		tmp.add(new Task(8,1,4));
		tmp.add(new Task(10,3,4));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(10,3,4));
		tmp.add(new Task(6,1));
		tmp.add(new Task(8,1,4));
		return tmp.getTM();
	}
	public static TaskMng ts3()	{
		return null;
	}
	public static TaskMng ts4()	{
		return null;
	}

	public static TaskMng ts5()	{
		return null;
	}
	public static TaskMng ts6()	{
		return null;
	}
	public static TaskMng ts7()	{
		return null;
	}

	public static TaskMng ts8()	{
		return null;
	}
	

	public static TaskMng getTS(int no) {
		if(no==1)
			return ts1();
		else if(no==2)
			return ts2();
		else if(no==3)
			return ts3();
		else if(no==4)
			return ts4();
		else if(no==5)
			return ts5();
		else if(no==6)
			return ts6();
		else if(no==7)
			return ts7();
		else if(no==8)
			return ts8();
		else
			return null;
	}


}
