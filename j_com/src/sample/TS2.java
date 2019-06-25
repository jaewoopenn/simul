package sample;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public class TS2 {
	public static TaskSet tm1() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(5,1));
		ts.add(new Task(12,2));
		ts.add(new Task(18,4));
		return new TaskSet(ts);
	}
	
	
}
