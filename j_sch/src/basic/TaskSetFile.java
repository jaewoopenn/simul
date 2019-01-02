package basic;

import java.util.Vector;
import util.FUtil;
import util.FUtilSp;
import util.Log;

public class TaskSetFile {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	public SysInfo g_info;
	public TaskSetFile() {
		g_info=new SysInfo();
		g_tasks=new TaskSet();
		g_lo_tasks=new TaskSet();
		g_hi_tasks=new TaskSet();
	}
	

	public TaskSetFile(Vector<Task> all) {
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
	public TaskSetFile(TaskSet ts) {
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
		
	


	public TaskSet getTS() {
		return g_tasks;
	}
	

	// static 
	public static void writeFile(String fn,TaskSet tasks){
		FUtil fu=new FUtil(fn);
		for(Task t:tasks.getArr())
			writeTask(fu,t);
		fu.save();
	}
	
	public static void writeTS(FUtil fu,TaskSet tasks){
		for(Task t:tasks.getArr())
			writeTask(fu,t);
		fu.write("------");
	}
	

	public static void writeTask(FUtil fu, Task t) {
		int isHI=t.is_HI?1:0;
		String txt=t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
		fu.write(txt);
	}
	public static void loadView(FUtil fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	Log.prn(1,line);
		}
	}

	// import 
	public static TaskSetFile loadFile(String f) {
		FUtilSp fu=new FUtilSp(f);
	    fu.load();
	    return TaskSetFile.loadFile_in(fu);
	}
	
	public static TaskSetFile  loadFile_in(FUtil fu) {
	    Vector<Task> tasks=new Vector<Task>();
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
//	    	Log.prn(1, line);
	    	Task t=loadTask(line);
        	tasks.add(t);
	    }
	    return new TaskSetFile(tasks);
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
