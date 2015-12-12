package Util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

public class FUtil {
	private String g_fn;
	private Vector<String> g_v;
	public FUtil(String file) {
		g_fn=file;
		g_v=new Vector();
	}
	public void print(String txt){
		g_v.add(txt);
	}
	public void end()
	{
		PrintWriter writer;
		try {
			writer = new PrintWriter("/Users/jaewoo/data/"+g_fn);
			for (String s:g_v){
				writer.println(s);
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	

}
