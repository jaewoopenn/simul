package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
/*


*/

public class MFile {
	protected String g_fn;
	protected BufferedReader g_br;
	protected Vector<String> g_v;
	public MFile(String file) {
		g_fn=file;
		g_v=new Vector<String>();
	}
	public int bufferSize(){
		return g_v.size();
	}
	public String getBuf(int idx){
		return g_v.elementAt(idx);
	}
	public void view() {
		for(int i=0;i<bufferSize();i++) {
			SLog.prn(1, getBuf(i));
		}
	}
	public void br_open() {
		if(g_fn==null)
			return;
	    File file = new File(MDir.path+g_fn);
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
}
