package task;


/*
 * TaskSetEx : different type of task sets, getTaskMng, saveFile
 * 
 * 
 */

import java.util.Vector;

import util.MList;
import util.SLog;

public class TaskUtil {


	
		
	



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
	public static void writeCom(MList fu,Task[] tasks){
		for(Task t:tasks)
			writeTask(fu,t);
		fu.add("+++++");
	}
	

	public static void writeTask(MList fu, Task t) {
		String txt=t.period+",";
		txt+=(int)t.exec+",";
		txt+=(int)t.deadline;
		fu.add(txt);
	}
	public static void loadView(MList fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	SLog.prn(1,line);
		}
	}

	// import 
	
	public static TaskVec  loadML(MList ml) {
		TaskSeq.reset();
	    Vector<Task> tasks=new Vector<Task>();
		for(int i=0;i<ml.size();i++) {
	    	String line=ml.get(i);
//	    	SLog.prn(1, line);
	    	Task t=loadTask(line);
        	tasks.add(t);
	    }
	    return new TaskVec(tasks);
	}
	
	public static Task loadTask(String line){
        String[] words=line.split(",");
        int p=Integer.valueOf(words[0]).intValue();
        int c=Integer.valueOf(words[1]).intValue();
        int d=Integer.valueOf(words[2]).intValue();
    	return new Task(p,c,d);
	}
	public static Vector<MList>  loadComML(MList o_ml) {
		Vector<MList> mlv=new Vector<MList>();
		MList tml=new MList();
		for(int i=0;i<o_ml.size();i++) {
	    	String line=o_ml.get(i);
	    	if(line.equals("+++++")) {
	    		mlv.add(tml);
	    		tml=new MList();
	    	} else {
	    		tml.add(line);
	    	}
	    }
	    return mlv;
	}
	

	

}
