package Basic;

import java.util.Vector;

import Util.FUtil;



public class CompGenFile {
	public static void writeFile(String file,Vector<Task> g_tasks) {
		FUtil fu=new FUtil(file);
		for(Task t:g_tasks)
		{
			int isHI=t.is_HI?1:0;
			String txt=t.tid+","+t.period+","+t.c_l+","+t.c_h+","+isHI;
			fu.print(txt);
		}
		fu.save();
	}

	public static void writeFile2(String file,Vector<Task> g_tasks) {
		FUtil fu=new FUtil(file);
		for(Task t:g_tasks)
		{
			int isHI=t.is_HI?1:0;
			String txt=t.tid+","+t.period+","+t.c_l+","+t.c_h+","+isHI+","+t.cid;
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
	public static Vector<Task>  loadFile2(String f) {
	    Vector<Task> g_tasks=new Vector<Task>();
	    FUtil fu=new FUtil(f);
	    fu.load();
	    Task tsk;
	    for(int i=0;i<fu.size();i++){
	    	String line=fu.get(i);
            String[] words=line.split(",");
            int tid=Integer.valueOf(words[0]).intValue();
            int p=Integer.valueOf(words[1]).intValue();
            double l=Double.valueOf(words[2]).doubleValue();
            double h=Double.valueOf(words[3]).doubleValue();
            int isHI=Integer.valueOf(words[4]).intValue();
            if(isHI==1)
            	tsk=new Task(tid,p,l,h);
            else
            	tsk=new Task(tid,p,l);
            int cid=Integer.valueOf(words[5]).intValue();
            tsk.setCom(cid);
        	g_tasks.add(tsk);
	    }
	    return g_tasks;
	}
	
}
