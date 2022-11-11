package util;

import java.io.File;
import java.io.PrintWriter;
/*


*/

public class MOut {
	private PrintWriter g_writer=null;
	
	public MOut(String file) {
		try {
			g_writer = new PrintWriter(MFile.path+file);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public void write(String txt){
		g_writer.println(txt);
	}
	public void save()
	{
		g_writer.close();
	}
	
	public static void makeDir(String str) {
		 File theDir = new File(MFile.path+str);
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
