package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;



public class TaskGenFile {
	public static void writeFile(String file,Vector<Task> g_tasks) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("/Users/jaewoo/data/"+file);
			for(Task t:g_tasks)
			{
				writer.println(t.tid+","+t.period+","+t.c_l);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
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
	            int e=Integer.valueOf(words[2]).intValue();
				g_tasks.add(new Task(tid,p,e));
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
