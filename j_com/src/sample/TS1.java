package sample;

import task.Task;
import task.TaskMng;
import task.TaskSet;

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
