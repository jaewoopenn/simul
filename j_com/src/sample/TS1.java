package sample;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public class TS1 {
	public static TaskSet tm1() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(6,1,6));
		ts.add(new Task(4,1,4));
		return new TaskSet(ts);
	}
	
	public static TaskSet tm2() {
		TaskVec ts=new TaskVec();
		ts.add(new Task(40,3,40));
		ts.add(new Task(24,1,24));
		ts.add(new Task(8,1,8));
		return new TaskSet(ts);
	}
	public static TaskSet tm3() {
		TaskVec ts=new TaskVec();
		ts.addTask(72,6,72); //1
		ts.addTask(103,3,103); //2
		ts.addTask(277,20,277); //3
		ts.addTask(240,16,240); //4
		ts.addTask(134,3,134); //5
		ts.addTask(291,21,291); //6
		ts.addTask(141,10,141); //7
		ts.addTask(258,19,258); //8
		ts.addTask(230,13,230);		//9
		return new TaskSet(ts);
	}
	public static TaskSet tm4() {
		TaskVec ts=new TaskVec();
		ts.addTask(103,3,103);
		ts.addTask(277,20,277);
		ts.addTask(240,16,240);
		ts.addTask(134,3,134);
		ts.addTask(72,6,72);
		ts.addTask(291,21,291);
		ts.addTask(141,10,141);
		ts.addTask(258,19,258);
		ts.addTask(230,13,230);		
		return new TaskSet(ts);
	}
	
}
