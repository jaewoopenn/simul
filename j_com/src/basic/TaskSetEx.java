package basic;


/*
 * TaskSetEx : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */

import java.util.Vector;

import util.FOut;
import util.MFile;
import util.S_Log;

public class TaskSetEx {
	private TaskSet g_tasks;


	public TaskSetEx(Vector<Task> all_tasks) {
		g_tasks=new TaskSet();
		for(int i=0;i<all_tasks.size();i++) {
			g_tasks.add(all_tasks.get(i));
		}
	}
	
	public void stat(){
		S_Log.prn(2, g_tasks.v_size());
	}
	
	// export 
	public TaskMng getTM()
	{
		g_tasks.end();
		return new TaskMng(g_tasks);
	}
		
	



	// static 
	public static void writeFile(String fn,Task[] tasks){
		FOut fu=new FOut(fn);
		for(Task t:tasks)
			writeTask(fu,t);
		fu.save();
	}
	
	public static void writeTS(FOut fu,Task[] tasks){
		for(Task t:tasks)
			writeTask(fu,t);
		fu.write("------");
	}
	

	public static void writeTask(FOut fu, Task t) {
		String txt=t.period+",";
		txt+=(int)t.exec;
		fu.write(txt);
	}
	public static void loadView(MFile fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	S_Log.prn(1,line);
		}
	}

	// import 
	public static TaskSetEx loadFile(String f) {
		MFile fu=new MFile(f);
	    fu.load();
	    return TaskSetEx.loadFile_in(fu);
	}
	
	public static TaskSetEx  loadFile_in(MFile fu) {
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
