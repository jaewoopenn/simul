package basic;

import java.util.Vector;

import utilSim.FUtil;



public class TaskFile {
	public static void writeFile(String file,Vector<Task> g_tasks) {
		FUtil fu=new FUtil(file);
		for(Task t:g_tasks)
		{
			int isHI=t.is_HI?1:0;
			String txt=t.tid+","+t.period+",";
			txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
			fu.print(txt);
		}
		fu.save();
	}

	public static void writeFile(String file,Task[] g_tasks) {
		FUtil fu=new FUtil(file);
		for(Task t:g_tasks)
		{
			int isHI=t.is_HI?1:0;
			String txt=t.tid+","+t.period+",";
			txt+=(int)t.c_l+","+(int)t.c_h+","+isHI;
			fu.print(txt);
		}
		fu.save();
	}


	public static Vector<Task>  loadFile(String f) {
	    Vector<Task> g_tasks=new Vector<Task>();
	    FUtil fu=new FUtil(f);
	    fu.load();
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
            String[] words=line.split(",");
            int tid=Integer.valueOf(words[0]).intValue();
            int p=Integer.valueOf(words[1]).intValue();
            int l=Integer.valueOf(words[2]).intValue();
            int h=Integer.valueOf(words[3]).intValue();
            int isHI=Integer.valueOf(words[4]).intValue();
            if(isHI==1)
            	g_tasks.add(new Task(tid,p,l,h));
            else
            	g_tasks.add(new Task(tid,p,l));
	    }
	    return g_tasks;
	}
	
}
