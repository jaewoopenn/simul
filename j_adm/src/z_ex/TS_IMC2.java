package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskVec;

// Task Set MC
public class TS_IMC2 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(7,2,1,false));
		tmp.add(new Task(8,1,3));
		tmp.add(new Task(10,3,4));
		return tmp.getTM();
	}
	
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(6,2,1,false));
		tmp.add(new Task(8,1,3));
		tmp.add(new Task(10,3,4));
		return tmp.getTM();
	}
	public static TaskMng ts3()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(18,2,1,false));
		tmp.add(new Task(13,2,1,false));
		tmp.add(new Task(53,13));
		tmp.add(new Task(12,2,3));
		tmp.add(new Task(9,2,3));
		return tmp.getTM();
	}
	public static TaskMng ts4()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(13,2,6));
		tmp.add(new Task(17,1,2));
		tmp.add(new Task(15,2,3));
		tmp.add(new Task(15,3,1,false));
		tmp.add(new Task(20,2,1,false));
		return tmp.getTM();
	}

	public static TaskMng ts5()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(123,11,12));
		tmp.add(new Task(84,6));
		tmp.add(new Task(139,2,7));
		tmp.add(new Task(22,2,3));
		tmp.add(new Task(115,11,22));
		tmp.add(new Task(88,3,8));
		tmp.add(new Task(122,8));
		tmp.add(new Task(23,3));	
		return tmp.getTM();
	}
	public static TaskMng ts6()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(8,1,3));
		tmp.add(new Task(10,2,3));
		tmp.add(new Task(20,2,5));
		tmp.add(new Task(15,1));
		return tmp.getTM();
	}
	public static TaskMng ts7()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(102,14));
		tmp.add(new Task(145,12,26));
		tmp.add(new Task(74,9,10));
		tmp.add(new Task(107,17,18));
		return tmp.getTM();
	}

	public static TaskMng ts8()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(78, 1, 2));
		tmp.add(new Task(120, 8, 14));
		tmp.add(new Task(91, 7));
		tmp.add(new Task(116, 5));
		tmp.add(new Task(119, 18));
		tmp.add(new Task(107, 1, 4));
		tmp.add(new Task(116, 5, 10));
		tmp.add(new Task(32, 4, 6));
		return tmp.getTM();
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
