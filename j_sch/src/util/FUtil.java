package util;

import java.io.File;
/*


*/

// load func
// make dir 

public class FUtil {
	

	
	
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
