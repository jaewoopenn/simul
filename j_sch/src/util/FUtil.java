package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
/*


*/

public class FUtil {
	public static final String path="/data/";
	protected String g_fn;
	protected BufferedReader g_br;
	protected Vector<String> g_v;
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
			S_Log.prn(1, get(i));
		}
	}
	public void br_open() {
		if(g_fn==null)
			return;
	    File file = new File(FUtil.path+g_fn);
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

}
