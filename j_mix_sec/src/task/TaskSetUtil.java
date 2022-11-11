package task;

import util.MList;
import util.SLog;

/*
 * TaskSetUtil : loadFile saveFile
 * 
 * 
 */


public class TaskSetUtil {


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
		int isHI=t.isHC()?1:0;
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
	
	public static TaskSetMC  loadFile(MList fu) {
		TaskSeq.reset();
		TaskVec tasks=new TaskVec();
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
//	    	Log.prn(1, line);
	    	Task t=loadTask(line);
        	tasks.add(t);
	    }
	    return new TaskSetMC(new TaskSet(tasks));
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
