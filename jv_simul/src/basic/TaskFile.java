package basic;

import java.util.Vector;

import comp.Comp;
import utilSim.FUtil;



public class TaskFile {

	

	public static void writeTask(FUtil fu, Task t) {
		int isHI=t.is_HI?1:0;
		String txt=t.tid+","+t.period+",";
		txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
		fu.print(txt);
	}

	public static Vector<Task>  loadFile(FUtil fu,int st,int size) {
	    Vector<Task> tasks=new Vector<Task>();
	    for(int i=st;i<st+size;i++){
	    	String line=fu.get(i);
//	    	Log.prn(1, line);
	    	Task t=loadTask(line);
        	tasks.add(t);
	    }
	    return tasks;
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
	public static Comp loadComp(String line,FUtil fu,int st){
        String[] words=line.split(",");
        if(!words[0].equals("C"))
        	return null;
        int id=Integer.valueOf(words[1]).intValue();
        int num=Integer.valueOf(words[2]).intValue();
        double alpha=Double.valueOf(words[3]).doubleValue();
//        Log.prn(1,id+" "+num);
        Comp c=new Comp(alpha);
        c.setID(id);
        
        Vector<Task> tasks=loadFile(fu,st,num);
        TaskMngPre tmp=new TaskMngPre(tasks);
        c.setTM(tmp.freezeTasks());
        return c;
	}
	
}
