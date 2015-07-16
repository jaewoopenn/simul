package Simul;

import java.util.Vector;

public class TaskMng {
	private Vector<Task> taskV;
	private Task[] tasks;
	private int size;
	public TaskMng() {
		taskV=new Vector<Task>();
		size=0;
	}

	public void addTask(int p, int e) {
		taskV.add(new Task(size,p,e));
		size++;
	}
	public void toArray()
	{
		tasks=new Task[size];
		taskV.toArray(tasks);
	}
	public int size() {
		return size;
	}

	public int getPt(int i) {
		return tasks[i].period;
	}

	public int[] getPeriods() {
		int[] plst=new int[size];
		for(int i=0;i<size;i++)
		{
			plst[i]=getPt(i);
		}
		
		return plst;
	}

	public Task getTask(int i) {
		return tasks[i];
	}

}
