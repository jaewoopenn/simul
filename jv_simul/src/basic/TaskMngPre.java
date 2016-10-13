package basic;

import java.util.Vector;

import utill.FUtil;

public class TaskMngPre {
	private Vector<Task> g_taskV;
	private Vector<Task> g_lo_taskV;
	private Vector<Task> g_hi_taskV;
	private Task[] g_tasks;
	private Task[] g_lo_tasks;
	private Task[] g_hi_tasks;
	public TaskSetInfo g_info;
	public TaskMngPre() {
		g_taskV=new Vector<Task>();
		g_lo_taskV=new Vector<Task>();
		g_hi_taskV=new Vector<Task>();
		g_info=new TaskSetInfo();
	}
	
	public TaskMngPre(Vector<Task> tasks) {
		g_info=new TaskSetInfo();
		setTasks(tasks);
	}

	public void add(Task t){
		int tid=g_taskV.size();
		t.tid=tid;
		g_taskV.add(t);
		if(t.is_HI)
			g_hi_taskV.add(t);
		else
			g_lo_taskV.add(t);
	}
	public void setTasks(Vector<Task> all) {
		g_taskV=all;
		g_lo_taskV=new Vector<Task>();
		g_hi_taskV=new Vector<Task>();
		for(Task t:all){
			if(t.is_HI)
				g_hi_taskV.add(t);
			else
				g_lo_taskV.add(t);
		}
	}
	public void goToArray(){
		int size=g_taskV.size();
		int h_size=g_hi_taskV.size();
		int l_size=g_lo_taskV.size();
		g_tasks=new Task[size];
		g_hi_tasks=new Task[h_size];
		g_lo_tasks=new Task[l_size];
		g_taskV.toArray(g_tasks);
		g_hi_taskV.toArray(g_hi_tasks);
		g_lo_taskV.toArray(g_lo_tasks);
	}
	public TaskMng freezeTasks()
	{
		goToArray();
		
		double loutil=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		int lo_size=0;
		int hi_size=0;
		for(Task t:g_tasks)
		{
			double tu=t.getHiUtil();
			if(t.is_HI){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=tu;
				hi_size++;
			} else {
				loutil+=tu;
				lo_size++;
			}
		}
		g_info.setLo_util(loutil);
		g_info.setHi_util_hm(hiutil_hm);
		g_info.setHi_util_lm(hiutil_lm);
		g_info.setHi_size(hi_size);
		g_info.setLo_size(lo_size);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,g_info);
	}
	
//	public void loadFile(String f) {
//	}
	
	public static TaskMngPre loadFile(String f) {
	    FUtil fu=new FUtil(f);
	    fu.load();
	    Vector<Task> tasks=TaskFile.loadFile(fu,0,fu.size());
	    return new TaskMngPre(tasks);
	}

	


}
