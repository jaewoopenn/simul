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

				
		double loutil=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		for(Task t:g_tasks.getArr())
		{
			double tu=t.getHiUtil();
			if(t.isHC()){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=tu;
			} else {
				loutil+=tu;
			}
		}
		SysInfo info=new SysInfo();
		info.setLo_util(loutil);
		info.setUtil_HC_HI(hiutil_hm);
		info.setUtil_HC_LO(hiutil_lm);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,info);
	}
		
	





}
