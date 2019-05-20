package sample;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public class TS1 {
	public static TaskSet tm1() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(6,1));
		ts.add(new Task(4,1));
		return new TaskSet(ts);
	}
	
	public static TaskSet tm2() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(17,7));
		ts.add(new Task(14,1));
		ts.add(new Task(15,4));
		return new TaskSet(ts);
	}
	public static TaskSet tm3() {
		TaskVec ts=new TaskVec();
		ts.addTask(72,6); //1
		ts.addTask(103,3); //2
		ts.addTask(277,20); //3
		ts.addTask(240,16); //4
		ts.addTask(134,3); //5
		ts.addTask(291,21); //6
		ts.addTask(141,10); //7
		ts.addTask(258,19); //8
		ts.addTask(230,13);		//9
		return new TaskSet(ts);
	}
	public static TaskSet tm4() {
		TaskVec ts=new TaskVec();
		ts.addTask(103,3);
		ts.addTask(277,20);
		ts.addTask(240,16);
		ts.addTask(134,3);
		ts.addTask(72,6);
		ts.addTask(291,21);
		ts.addTask(141,10);
		ts.addTask(258,19);
		ts.addTask(230,13);		
		return new TaskSet(ts);
	}
	
}
