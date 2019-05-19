package sample;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public class TS1 {
	public static TaskSet tm1() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(4,1));
		ts.add(new Task(6,1));
		return new TaskSet(ts);
	}
	
	public static TaskSet tm2() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(4,1));
		ts.add(new Task(5,1));
		ts.add(new Task(7,2));
		return new TaskSet(ts);
	}
	
}
