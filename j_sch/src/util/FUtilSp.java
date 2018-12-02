package util;

import java.io.File;
import java.util.Vector;
/*


*/

public class FUtilSp extends FUtil {
	public FUtilSp(){
		super();
	}
	public FUtilSp(String file) {
		super(file);
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
		 File theDir = new File(FUtilSp.g_path+str);
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
