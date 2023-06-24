package util;

import java.io.File;

/*


*/

public class MDir {
	public static final String path=System.getProperty ( "user.home" )+"/data/";
//	public static final String path="./data/";
	public static void makeDir(String str) {
		 File theDir = new File(MDir.path+str);
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
	}	}
