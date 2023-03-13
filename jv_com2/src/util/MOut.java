package util;
//..

import java.io.PrintWriter;
/*


*/

public class MOut {
	private PrintWriter g_writer=null;
	
	public MOut(String file) {
		try {
			g_writer = new PrintWriter(MDir.path+file);
			
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
	

}
