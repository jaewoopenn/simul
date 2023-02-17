package task;

/*
 * TaskSetMC : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */



public class TaskSetMC {
	private TaskSet g_tasks;
	

	public TaskSetMC(TaskSet ts) {
		g_tasks=ts;
	}
	
	
	// export 
	public TaskMng getTM()
	{
		TaskSet g_hi_tasks;
		TaskSet g_lo_tasks;

		TaskVec hi_tasks=new TaskVec();
		TaskVec lo_tasks=new TaskVec();
		for(Task t:g_tasks.getArr()){
			if(t.isHC())
				hi_tasks.add(t);
			else
				lo_tasks.add(t);
		}
		g_hi_tasks=new TaskSet(hi_tasks);
		g_lo_tasks=new TaskSet(lo_tasks);
				
		double loutil=0;
		double loutil_de=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		for(Task t:g_tasks.getArr())
		{
			if(t.isHC()){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=t.getHiUtil();
			} else {
				loutil+=t.getLoUtil();
				loutil_de+=t.getHiUtil();
			}
		}
		SysInfo info=new SysInfo();
		info.setLo_util(loutil);
		info.setLo_de_util(loutil_de);
		info.setUtil_HC_HI(hiutil_hm);
		info.setUtil_HC_LO(hiutil_lm);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,info);
	}
		
	





}
