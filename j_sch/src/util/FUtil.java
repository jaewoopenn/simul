package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;


public class FUtil {
	public static final String g_path="D:/data/";
	private String g_fn;
	private Vector<String> g_v;
	public FUtil(){
		g_fn=null;
		g_v=new Vector<String>();
	}
	public FUtil(String file) {
		g_fn=file;
		g_v=new Vector<String>();
	}
	public int size(){
		return g_v.size();
	}
	public String get(int idx){
		return g_v.elementAt(idx);
	}
	public void print(String txt){
		if(g_fn!=null)
			g_v.add(txt);
	}
	public void save()
	{
		if(g_fn==null)
			return;
		PrintWriter writer;
		try {
			writer = new PrintWriter(g_path+g_fn);
			for (String s:g_v){
				writer.println(s);
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public void load(){
		if(g_fn==null)
			return;
	    File file = new File(g_path+g_fn);
		g_v=new Vector<String>();
		try {
			FileReader fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		    String line;
		    while((line = br.readLine()) != null){
		    	g_v.add(line);
		    }
		    br.close();
		    fr.close();	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void prn() {
		for(String s:g_v){
			Log.prn(1, s);
		}
		
	}
	public Vector<String> getVec() {
		return g_v;
	}
	public static void makeDir(String str) {
		 File theDir = new File(FUtil.g_path+str);
		if (theDir.exists()) {
//			System.out.println("dir exist");
			return;
		}
		try{
	        theDir.mkdir();
	    } 
	    catch(SecurityException se){
	        se.printStackTrace();
	    }        
	}

}
