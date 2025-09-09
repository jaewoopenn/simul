package task;

import util.MList;
import util.MLoop;
import util.SLog;

/*
 * TaskSetUtil : loadFile saveFile
 * 
 * 
 */


public class TSFile {
	private static int space=500;

	// static 
	public static void writeFile(String fn,Task[] tasks) {
		MList ml=MList.new_list();
		writeTS_in(ml, tasks);
		ml.saveTo(fn);
	}
	public static void writeTS(MList ml,Task[] tasks){
		writeTS_in(ml,tasks);		
		ml.add("------");
	}
	public static void writeTask(MList ml, Task t) {
		String txt=getTaskMsg(t);
		ml.add(txt);
	}
	private static void writeTS_in(MList ml,Task[] tasks){
		ml.add("stage,1");
		for(Task t:tasks) {
			String txt=getTaskMsg(t);
			ml.add(txt);
		}
	}	
	private static String getTaskMsg(Task t) {
		int isHI=t.isHC()?1:0;
		String txt="add,";
		txt+=t.tid+",";
		txt+=t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
		return txt;
	}
	
	public static void remove(MList ml, int i) {
		String txt="remove,";
		txt+=(int)i;
		ml.add(txt);
		
	}
	public static void initStage(MList ml, int n) {
		String txt="stage,";
		txt+=(int)n;
		ml.add(txt);
		
	}
	public static void nextStage(MList ml, int i) {
		String txt="next,"+(i*space);
//		SLog.prn(2, txt);
		ml.add(txt);
		
	}
	public static void loadView(MList ml) {
		for(int i:MLoop.on(ml.size())) {
	    	String line=ml.get(i);
	    	SLog.prn(1,line);
		}
	}

	// import 
	

	public static DTaskVec  loadFile(MList ml) {
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
	        	DTUtil.copy(tasks, stage-1,stage);
	        }
	    }
	    return tasks;
	}
	
	public static Task loadTask(String[] words){
        int tid=Integer.valueOf(words[1]).intValue();
        int p=Integer.valueOf(words[2]).intValue();
        int l=Integer.valueOf(words[3]).intValue();
        int h=Integer.valueOf(words[4]).intValue();
        int isHI=Integer.valueOf(words[5]).intValue();
        if(isHI==1)
        	return new Task(tid,p,l,h,true);
        else
        	return new Task(tid,p,l,h,false);
	       
	}



	

}
