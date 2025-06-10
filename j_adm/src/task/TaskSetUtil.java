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
		writeTS_in(fu,tasks);		
		fu.save(fn);
	}
	
	public static void writeTS(MList fu,Task[] tasks){
		writeTS_in(fu,tasks);		
		fu.add("------");
	}
	private static void writeTS_in(MList fu,Task[] tasks){
		fu.add("stage,1");
		for(Task t:tasks)
			writeTask(fu,t);
	}	
	
	public static void removeTask(MList fu, int i) {
		String txt="remove,";
		txt+=(int)i;
//		SLog.prn(2, txt);
		fu.add(txt);
		
	}
	public static void initStage(MList fu, int n) {
		String txt="stage,";
		txt+=(int)n;
//		SLog.prn(2, txt);
		fu.add(txt);
		
	}
	public static void nextStage(MList fu) {
		String txt="next";
//		SLog.prn(2, txt);
		fu.add(txt);
		
	}

	public static void writeTask(MList fu, Task t) {
		int isHI=t.isHC()?1:0;
		String txt="add,";
		txt+=t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
//		SLog.prn(2, txt);
		fu.add(txt);
	}
	public static void loadView(MList fu) {
		for(int i=0;i<fu.size();i++) {
	    	String line=fu.get(i);
	    	SLog.prn(1,line);
		}
	}

	// import 
	
	public static TaskSet  loadFile(MList fu) {
		TaskSeq.reset();
		DTaskVec tasks=new DTaskVec(3);
		Task t;
		int stage=0;
		for(int i=1;i<fu.size();i++) {
	    	String line=fu.get(i);
//	    	Log.prn(1, line);
	        String[] words=line.split(",");
        	t=loadTask(words);
        	tasks.add(stage,t);
	    }
	    return new TaskSet(tasks.getVec(0));
	}

	public static DTaskVec  loadFile2(MList fu) {
		TaskSeq.reset();
    	String line=fu.get(0);
//    	SLog.prn(2, line);
        String[] words=line.split(",");
        int num=Integer.valueOf(words[1]).intValue();
		DTaskVec tasks=new DTaskVec(num);
		Task t;
		int stage=0;
		for(int i=1;i<fu.size();i++) {
	    	line=fu.get(i);
//	    	Log.prn(1, line);
	        words=line.split(",");
	        if(words[0].equals("add")) {
	        	t=loadTask(words);
	        	tasks.add(stage,t);
	        } else if(words[0].equals("remove")) {
	        	int idx=Integer.valueOf(words[1]).intValue();
	        	tasks.remove(stage,idx);
	        } else if(words[0].equals("next")) {
	        	stage++;
	        	tasks.copy(stage-1,stage);
	        }
	    }
	    return tasks;
	}
	
	public static Task loadTask(String[] words){
        int p=Integer.valueOf(words[1]).intValue();
        int l=Integer.valueOf(words[2]).intValue();
        int h=Integer.valueOf(words[3]).intValue();
        int isHI=Integer.valueOf(words[4]).intValue();
        if(isHI==1)
        	return new Task(p,l,h);
        else
        	return new Task(p,l,h,false);
	       
	}



	

}
