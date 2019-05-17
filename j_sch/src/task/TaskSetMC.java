package task;

/*
 * TaskSetMC : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */


import util.SLog;

public class TaskSetMC {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	

	public TaskSetMC(TaskSet ts) {
		g_tasks=new TaskSet();
		g_lo_tasks=new TaskSet();
		g_hi_tasks=new TaskSet();
		for(Task t:ts.getVec()){
			g_tasks.add(t);
			if(t.is_HI)
				g_hi_tasks.add(t);
			else
				g_lo_tasks.add(t);
		}
	}
	
	public void stat(){
		SLog.prn(2, g_tasks.v_size());
	}
	
	// export 
	public TaskMng getTM()
	{

		g_tasks.end();
		g_hi_tasks.end();
		g_lo_tasks.end();
				
		double loutil=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		for(Task t:g_tasks.getArr())
		{
			double tu=t.getHiUtil();
			if(t.is_HI){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=tu;
			} else {
				loutil+=tu;
			}
		}
		SysInfo info=new SysInfo();
		info.setLo_util(loutil);
		info.setHi_util_hm(hiutil_hm);
		info.setHi_util_lm(hiutil_lm);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,info);
	}
		
	





}
