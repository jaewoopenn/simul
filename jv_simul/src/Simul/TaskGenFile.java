package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;

import Util.FUtil;



public class TaskGenFile {
	public static void writeFile(String file,Vector<Task> g_tasks) {
		FUtil fu=new FUtil(file);
		for(Task t:g_tasks)
		{
			int isHI=t.is_HI?1:0;
			String txt=t.tid+","+t.period+","+t.c_l+","+t.c_h+","+isHI;
			fu.print(txt);
		}
		fu.end();
	}


	public static Vector<Task>  loadFile(String f) {
	    File file = new File("/Users/jaewoo/data/"+f);
	    Vector<Task> g_tasks=new Vector<Task>();
		try {
			FileReader fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		    String line;
		    while((line = br.readLine()) != null){
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
		    br.close();
		    fr.close();	
		    return g_tasks;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}
