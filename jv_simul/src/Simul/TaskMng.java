package Simul;

import java.util.Vector;

public class TaskMng {
	private boolean bAdd;
	private Vector<Task> taskV;
	private Task[] tasks;
	private int size;
	public TaskMng() {
		taskV=new Vector<Task>();
		size=0;
		bAdd=true;
	}

	public void addTask(int p, int e) {
		if(!bAdd) {
			System.out.println("Err:task set is finalized");
			return;
		}
		taskV.add(new Task(size,p,e));
		size++;
	}
	public void finalize()
	{
		bAdd=false;
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
