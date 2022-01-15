package z_ex;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;

// Task Set MC
public class TS_MC5 {
	
	public static TaskMng ts1()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(139, 7));
		tmp.add(new Task(97, 17));
		tmp.add(new Task(101, 6));
		tmp.add(new Task(61, 10));
		tmp.add(new Task(143, 14, 23));
		tmp.add(new Task(146, 24));
		tmp.add(new Task(22, 4));
		tmp.add(new Task(142, 10, 20));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}
	public static TaskMng ts2()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(93, 3, 10));
		tmp.add(new Task(55, 1));
		tmp.add(new Task(129, 11, 25));
		tmp.add(new Task(141, 26));
		tmp.add(new Task(71, 11));
		tmp.add(new Task(119, 10));
		tmp.add(new Task(145, 24));
		tmp.add(new Task(64, 1, 5));
		tmp.add(new Task(39, 1, 3));
		tmp.add(new Task(101, 13));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}	

	public static TaskMng ts3()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(89, 14));
		tmp.add(new Task(126, 2));
		tmp.add(new Task(55, 2, 8));
		tmp.add(new Task(78, 15));
		tmp.add(new Task(126, 6));
		tmp.add(new Task(103, 3));
		tmp.add(new Task(121, 18));
		tmp.add(new Task(100, 5));
		tmp.add(new Task(91, 8));
		tmp.add(new Task(100, 18));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}	

	public static TaskMng ts4()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(60, 2, 5));
		tmp.add(new Task(127, 1, 2));
		tmp.add(new Task(70, 12));
		tmp.add(new Task(149, 2, 6));
		tmp.add(new Task(117, 10, 17));
		tmp.add(new Task(80, 5));
		tmp.add(new Task(36, 6));
		tmp.add(new Task(93, 7, 12));
		tmp.add(new Task(101, 10));
		tmp.add(new Task(29, 1));
		tmp.add(new Task(90, 7));
		tmp.add(new Task(49, 1));
		tmp.add(new Task(123, 12));		
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
	}	

	public static TaskMng ts5()	{
		TaskVec tmp=new TaskVec();
		tmp.add(new Task(91, 1, 3));
		tmp.add(new Task(107, 16));
		tmp.add(new Task(48, 7));
		tmp.add(new Task(111, 11));
		tmp.add(new Task(101, 2, 6));
		tmp.add(new Task(54, 1));
		tmp.add(new Task(56, 6, 7));
		tmp.add(new Task(78, 5, 14));
		tmp.add(new Task(68, 3));
		tmp.add(new Task(80, 3, 11));
		tmp.add(new Task(146, 7));
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		return tme.getTM();
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



}
