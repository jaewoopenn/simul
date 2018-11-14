package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;
/*
TODO Change load all to load step by step 


*/

public class FUtil {
	public static final String g_path="/data/";
	private boolean isWrite=true;
	private String g_fn;
	private BufferedReader g_br;
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
	public void view() {
		for(int i=0;i<size();i++) {
			Log.prn(1, get(i));
		}
	}
	public void write(String txt){
		if(!isWrite) {
			Log.err("read mode, attempt write");
			System.exit(1);
		}
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
	public void br_open() {
		if(g_fn==null)
			return;
	    File file = new File(g_path+g_fn);
		try {
			FileReader fr = new FileReader(file);
			g_br = new BufferedReader(fr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		isWrite=true;
	}
	public String read() {
		if(isWrite) {
			Log.err("write mode, attempt read");
			System.exit(1);
		}
		String line=null;
		try {
			line = g_br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return line;
	}
	public void br_close() {
		try {
			g_br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public boolean readSplit(String split) {
		g_v=new Vector<String>();
		try {
		    String line;
		    while((line =g_br.readLine()) != null){
		    	if(line.equals(split)) {
		    		return true;
		    	}
		    	g_v.add(line);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return false;
		
	}
	public int load(){
		br_open();
		g_v=new Vector<String>();
		try {
		    String line;
		    while((line = g_br.readLine()) != null){
		    	g_v.add(line);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		br_close();
		return g_v.size();
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
