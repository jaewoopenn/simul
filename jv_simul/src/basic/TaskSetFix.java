package basic;

import java.util.Vector;
import util.FUtil;
import util.Log;

public class TaskSetFix {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	public SysInfo g_info;
	public TaskSetFix() {
		g_info=new SysInfo();
		g_tasks=new TaskSet();
		g_lo_tasks=new TaskSet();
		g_hi_tasks=new TaskSet();
	}
	

	public TaskSetFix(Vector<Task> all) {
		g_info=new SysInfo();
		g_tasks=new TaskSet();
		g_lo_tasks=new TaskSet();
		g_hi_tasks=new TaskSet();
		for(Task t:all){
			g_tasks.add(t);
			if(t.is_HI)
				g_hi_tasks.add(t);
			else
				g_lo_tasks.add(t);
		}
	}
	public TaskSetFix(TaskSet ts) {
		g_info=new SysInfo();
		g_tasks=ts;
		g_lo_tasks=new TaskSet();
		g_hi_tasks=new TaskSet();
		for(Task t:ts.getArr()){
			if(t.is_HI)
				g_hi_tasks.add(t);
			else
				g_lo_tasks.add(t);
		}		
	}


	public void addTID(Task t){
		int tid=g_tasks.v_size();
		t.tid=tid;
		add(t);
	}
	
	public void add(Task t){
		g_tasks.add(t);
		if(t.is_HI)
			g_hi_tasks.add(t);
		else
			g_lo_tasks.add(t);
	}
	
	
	
	public void stat(){
		Log.prn(2, g_tasks.v_size());
	}
	
	// export 
	public TaskMng getTM()
	{
		g_tasks.transform_Array();
		g_hi_tasks.transform_Array();
		g_lo_tasks.transform_Array();
				
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
		g_info.setLo_util(loutil);
		g_info.setHi_util_hm(hiutil_hm);
		g_info.setHi_util_lm(hiutil_lm);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,g_info);
	}
		
	
	// import 
	public static TaskSetFix loadFile(String f) {
	    FUtil fu=new FUtil(f);
	    fu.load();
	    Vector<Task> tasks=TaskFile.loadFile(fu,0,fu.size());
	    return new TaskSetFix(tasks);
	}


	public TaskSet getTS() {
		return g_tasks;
	}
	




}
