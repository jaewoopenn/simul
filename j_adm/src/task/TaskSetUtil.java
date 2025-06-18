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
		fu.saveTo(fn);
	}
	
	public static void writeTS(MList ml,Task[] tasks){
		writeTS_in(ml,tasks);		
		ml.add("------");
	}
	private static void writeTS_in(MList ml,Task[] tasks){
		ml.add("stage,1");
		for(Task t:tasks)
			writeTask(ml,t);
	}	
	
	public static void remove(MList ml, int i) {
		String txt="remove,";
		txt+=(int)i;
//		SLog.prn(2, txt);
		ml.add(txt);
		
	}
	public static void initStage(MList fu, int n) {
		String txt="stage,";
		txt+=(int)n;
//		SLog.prn(2, txt);
		fu.add(txt);
		
	}
	public static void nextStage(MList ml, int i) {
		String txt="next,"+(i*100);
//		SLog.prn(2, txt);
		ml.add(txt);
		
	}

	public static void writeTask(MList ml, Task t) {
		int isHI=t.isHC()?1:0;
		String txt="add,";
		txt+=t.tid+",";
		txt+=t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
//		SLog.prn(2, txt);
		ml.add(txt);
	}
	public static void loadView(MList ml) {
		for(int i=0;i<ml.size();i++) {
	    	String line=ml.get(i);
	    	SLog.prn(1,line);
		}
	}

	// import 
	
	public static TaskSet  loadFile(MList ml) {
		TaskSeq.reset();
		DTaskVec tasks=new DTaskVec(3);
		Task t;
		int stage=0;
		for(int i=1;i<ml.size();i++) {
	    	String line=ml.get(i);
//	    	SLog.prn(1, line);
	        String[] words=line.split(",");
	        if(words[0].equals("add")) {
	        	t=loadTask(words);
	        	tasks.add(stage,t);
	        }
	    }
	    return new TaskSet(tasks.getVec(0));
	}

	public static DTaskVec  loadFile2(MList ml) {
		TaskSeq.reset();
    	String line=ml.get(0);
//    	SLog.prn(2, line);
        String[] words=line.split(",");
        int num=Integer.valueOf(words[1]).intValue();
		DTaskVec tasks=new DTaskVec(num);
    	tasks.addTime(0,0);
		Task t;
		int stage=0;
		for(int i=1;i<ml.size();i++) {
	    	line=ml.get(i);
//	    	SLog.prn(2, line);
	        words=line.split(",");
	        if(words[0].equals("add")) {
	        	t=loadTask(words);
	        	tasks.add(stage,t);
	        } else if(words[0].equals("remove")) {
	        	int idx=Integer.valueOf(words[1]).intValue();
	        	tasks.remove(stage,idx);
	        } else if(words[0].equals("next")) {
	        	stage++;
//	        	SLog.prn(1, words[1]);
	        	int time=Integer.valueOf(words[1]).intValue();
	        	tasks.addTime(stage, time);
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
        	return new Task(p,l,h,true);
        else
        	return new Task(p,l,h,false);
	       
	}



	

}
