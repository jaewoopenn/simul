package basic;

/*
 * TaskSetEx : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */


import util.MList;
import util.SLog;

public class TaskSetEx {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	public SysInfo g_info;
	

	public TaskSetEx(TaskSet ts) {
		g_info=new SysInfo();
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
		g_info.setLo_util(loutil);
		g_info.setHi_util_hm(hiutil_hm);
		g_info.setHi_util_lm(hiutil_lm);
		return new TaskMng(g_tasks,g_hi_tasks,g_lo_tasks,g_info);
	}
		
	



	// static 
	public static void writeFile(String fn,Task[] tasks){
		MList fu=new MList();
		for(Task t:tasks)
			writeTask(fu,t);
		fu.save(fn);
	}
	
	public static void writeTS(MList fu,Task[] tasks){
		for(Task t:tasks)
			writeTask(fu,t);
		fu.add("------");
	}
	
	

	public static void writeTask(MList fu, Task t) {
		int isHI=t.is_HI?1:0;
		String txt=t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
		fu.add(txt);
	}
	public static void loadView(MList fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	SLog.prn(1,line);
		}
	}

	// import 
	public static TaskSetEx loadFile(String f) {
	    return TaskSetEx.loadFile_in(new MList(f));
	}
	
	public static TaskSetEx  loadFile_in(MList fu) {
		TaskSeq.reset();
		TaskSet tasks=new TaskSet();
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
//	    	Log.prn(1, line);
	    	Task t=loadTask(line);
        	tasks.add(t);
	    }
	    return new TaskSetEx(tasks);
	}
	
	public static Task loadTask(String line){
        String[] words=line.split(",");
        int p=Integer.valueOf(words[0]).intValue();
        int l=Integer.valueOf(words[1]).intValue();
        int h=Integer.valueOf(words[2]).intValue();
        int isHI=Integer.valueOf(words[3]).intValue();
        if(isHI==1)
        	return new Task(p,l,h);
        else
        	return new Task(p,l);
	}
	


}
