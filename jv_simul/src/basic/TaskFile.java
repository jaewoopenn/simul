package basic;

import java.util.Vector;

import utilSim.FUtil;



public class TaskFile {
//	public static void writeFile(String file,Vector<Task> g_tasks) {
//		FUtil fu=new FUtil(file);
//		for(Task t:g_tasks)
//			writeTask(fu,t);
//		fu.save();
//	}

	

	public static void writeTask(FUtil fu, Task t) {
		int isHI=t.is_HI?1:0;
		String txt=t.tid+","+t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
		fu.print(txt);
	}

	public static Vector<Task>  loadFile(FUtil fu) {
	    Vector<Task> g_tasks=new Vector<Task>();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
	    	Task t=loadTask(line);
        	g_tasks.add(t);
	    }
	    return g_tasks;
	}
	public static Task loadTask(String line){
        String[] words=line.split(",");
        int tid=Integer.valueOf(words[0]).intValue();
        int p=Integer.valueOf(words[1]).intValue();
        int l=Integer.valueOf(words[2]).intValue();
        int h=Integer.valueOf(words[3]).intValue();
        int isHI=Integer.valueOf(words[4]).intValue();
        if(isHI==1)
        	return new Task(tid,p,l,h);
        else
        	return new Task(tid,p,l);
	}
}
