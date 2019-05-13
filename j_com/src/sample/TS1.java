package sample;

import basic.Task;
import basic.TaskMng;
import basic.TaskSet;

public class TS1 {
	public static TaskMng getTM1() {
		TaskSet ts=new TaskSet();
		ts.add(new Task(4,1));
		ts.add(new Task(6,1));
		ts.end();
		return new TaskMng(ts);
	}
	
	public static TaskMng getTM2() {
		TaskSet ts=new TaskSet();
		ts.add(new Task(4,1));
		ts.add(new Task(5,1));
		ts.add(new Task(7,2));
		ts.end();
		return new TaskMng(ts);
	}
	
}
