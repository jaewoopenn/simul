package sysEx;

import part.CoreMng;
import basic.Task;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFix;

// Task Set MC
public class TS_MP1 {
	public static TaskMng ts1()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,16,3));
		tmp.add(new Task(0,27,2));
		tmp.add(new Task(0,6,1,2));
		tmp.add(new Task(0,8,1,3));
		tmp.add(new Task(0,14,2,3));
		return tmp.getTM();
	}

	public static TaskMng ts2()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,8,1));
		tmp.add(new Task(0,16,3));
		tmp.add(new Task(0,27,2));
		tmp.add(new Task(0,27,2));
		tmp.add(new Task(0,6,1,2));
		tmp.add(new Task(0,24,1,2));
		tmp.add(new Task(0,28,4,5));
		tmp.add(new Task(0,20,2,3));
		tmp.add(new Task(0,8,1,3));
		tmp.add(new Task(0,8,1,3));
		tmp.add(new Task(0,14,2,3));
		return tmp.getTM();
	}
	
	public static TaskMng ts3()	{
		TaskSetFix tmp=new TaskSetFix();
		tmp.add(new Task(0,4,2));
		tmp.add(new Task(1,6,1,5));
		tmp.add(new Task(1,6,1,5));
		return tmp.getTM();
	}

	public static CoreMng core1() {
		CoreMng cm=new CoreMng(2);
		TaskSet ts=new TaskSet();
		ts.add(new Task(0,3,1));
		ts.add(new Task(1,4,1));
		ts.add(new Task(2,7,1));
		ts.transform_Array();
		cm.setTS(0,ts);
		
		ts=new TaskSet();
		ts.add(new Task(0,14,1));
		ts.add(new Task(1,17,1));
		ts.transform_Array();
		cm.setTS(1,ts);
		
		return cm;
	}
	

	public static CoreMng core2() {
		CoreMng cm=new CoreMng(2);
		TaskSet ts=new TaskSet();
		ts.add(new Task(0,4,2));
		ts.add(new Task(1,6,1,5));
		ts.transform_Array();
		cm.setTS(0,ts);
		
		ts=new TaskSet();
		ts.add(new Task(0,12,1));
		ts.transform_Array();
		cm.setTS(1,ts);
		
		return cm;
	}
	
	public static CoreMng core3() {
		CoreMng cm=new CoreMng(2);
		TaskSet ts=new TaskSet();
		ts.add(new Task(0,6,1,5));
		ts.add(new Task(1,4,1));
		ts.add(new Task(2,8,2));
		ts.transform_Array();
		cm.setTS(0,ts);
		
		ts=new TaskSet();
		ts.add(new Task(3,6,1,5));
//		ts.add(new Task(1,4,1));
		ts.transform_Array();
		cm.setTS(1,ts);
		
		return cm;
	}

}
