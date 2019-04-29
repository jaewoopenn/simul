package util;

import java.io.PrintWriter;
/*


*/

public class FOut {
	private PrintWriter g_writer=null;
	
	public FOut(String file) {
		try {
			g_writer = new PrintWriter(FUtil.path+file);
			
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
