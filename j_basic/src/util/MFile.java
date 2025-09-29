package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
/*


*/

public class MFile {
	public static final String path=System.getProperty ( "user.home" )+"/data/";
	protected String g_fn;
	protected BufferedReader g_br;
	protected Vector<String> g_v;
	public MFile(String file) {
		if(file==null) {
			SLog.err("MFile: filename is null");
		}

		g_fn=file;
		g_v=new Vector<String>();
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
	public void prn() {
		for(int i=0;i<bufferSize();i++) {
			SLog.prn(getBuf(i));
		}
		
	}	
	public int bufferSize(){
		return g_v.size();
	}
	public String getBuf(int idx){
		return g_v.elementAt(idx);
	}
	public void br_open() {
		if(g_fn==null)
			return;
	    File file = new File(MFile.path+g_fn);
		try {
			FileReader fr = new FileReader(file);
			g_br = new BufferedReader(fr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public String read() {
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
	public boolean readUntil(String split) {
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
	public static void makeDir(String str) {
		 File theDir = new File(MFile.path+str);
		if (theDir.exists()) {
			System.out.println("dir exist");
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
