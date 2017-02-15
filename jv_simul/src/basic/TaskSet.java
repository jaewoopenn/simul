package basic;


public class TaskSet {
	private Task[] g_tasks;

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
//	public void set(Task[] a){
//		g_tasks=a;
//	}
	public Task get(int i){
		return g_tasks[i];
	}
	public int size(){
		return g_tasks.length;
	}
	public Task[] getArr(){
		return g_tasks;
	}

}
