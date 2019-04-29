package basic;


/*
 * TaskSetEx : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */

import java.util.Vector;

import util.FOut;
import util.FUtil;
import util.FUtilSp;
import util.S_Log;

public class TaskSetEx {
	private TaskSet g_tasks;
	
	public TaskSetEx() {
		g_tasks=new TaskSet();
	}
	

	public TaskSetEx(Vector<Task> all) {
		g_tasks=new TaskSet();
		for(Task t:all){
			g_tasks.add(t);
		}
	}
	
	public void stat(){
		S_Log.prn(2, g_tasks.v_size());
	}
	
	// export 
	public TaskMng getTM()
	{
		g_tasks.transform_Array();
				
		return new TaskMng(g_tasks);
	}
		
	



	// static 
	public static void writeFile(String fn,TaskSet tasks){
		FOut fu=new FOut(fn);
		for(Task t:tasks.getArr())
			writeTask(fu,t);
		fu.save();
	}
	
	public static void writeTS(FOut fu,TaskSet tasks){
		for(Task t:tasks.getArr())
			writeTask(fu,t);
		fu.write("------");
	}
	

	public static void writeTask(FOut fu, Task t) {
		String txt=t.period+",";
		txt+=(int)t.exec;
		fu.write(txt);
	}
	public static void loadView(FUtil fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	S_Log.prn(1,line);
		}
	}

	// import 
	public static TaskSetEx loadFile(String f) {
		FUtilSp fu=new FUtilSp(f);
	    fu.load();
	    return TaskSetEx.loadFile_in(fu);
	}
	
	public static TaskSetEx  loadFile_in(FUtil fu) {
		TaskSeq.reset();
	    Vector<Task> tasks=new Vector<Task>();
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
        int c=Integer.valueOf(words[1]).intValue();
    	return new Task(p,c);
	}
	


}
