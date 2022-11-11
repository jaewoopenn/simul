package task;

/*
 * TaskSetMC : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */



public class TaskSetMC {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	

	public TaskSetMC(TaskSet ts) {
		TaskVec tasks=new TaskVec();
		TaskVec hi_tasks=new TaskVec();
		TaskVec lo_tasks=new TaskVec();
		for(Task t:ts.getArr()){
			tasks.add(t);
			if(t.isHC())
				hi_tasks.add(t);
			else
				lo_tasks.add(t);
		}
		g_tasks=new TaskSet(tasks);
		g_hi_tasks=new TaskSet(hi_tasks);
		g_lo_tasks=new TaskSet(lo_tasks);
	}
	
	
	// export 
	public TaskMng getTM()
	{

		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks);
	}
		
	





}
