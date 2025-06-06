/* 
 * Static Class
 */

package util;

import java.io.PrintWriter;

public class SLogF {
	private static PrintWriter g_writer=null;
	private static boolean g_is_gen=false;
	
	public static boolean isON() {
		return (g_writer!=null);
	}
	
	public static void init(String file) {
		try {
			g_writer = new PrintWriter(MDir.path+file);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public static void initScr() {
		try {
			g_writer = new PrintWriter(System.out);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public static void setGen() {
		g_is_gen=true;
	}
	public static void prn(String txt){
		if(g_writer!=null&&g_is_gen==false)
			g_writer.println(txt);
	}
	public static void prnc(String txt){
		if(g_writer!=null&&g_is_gen==false)
			g_writer.print(txt);
	}
	public static void prng(String txt){
		if(g_writer!=null&&g_is_gen==true)
			g_writer.println(txt);
	}
	
	public static void save()
	{
		if(g_writer!=null) {
			g_writer.close();
			g_writer=null;
		} else {
			SLog.err("need FLOG init");
		}
	}
	
	// to prevent warning
	public static void alive() {
	
		
	}
}
