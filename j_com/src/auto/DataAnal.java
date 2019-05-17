package auto;

import java.util.Vector;

import util.MList;
import util.MOut;


public class DataAnal {
	private String g_path;
	private Vector<String> g_xl=new Vector<String>();
	private double[][] g_rs;
	private int g_max=0;
	private int g_xlen=0;
	public DataAnal(String path,int x) {
		g_path=path;
		g_max=x;
	}
	public void load_x(String fn) {
		MList fu=new MList();
		fu.load(g_path+"/"+fn);
		g_xlen=fu.size();
		String s;
		while((s=fu.getNext())!=null) {
			g_xl.add(s);
		}
		g_rs=new double[10][g_xlen];
	}
	public void load_rs(String fn){
		MList fu=new MList();
		fu.load(g_path+"/"+fn);
		int size=fu.size();
		g_max=size;
		String s;
		int i=0;
		while((s=fu.getNext())!=null) {
			load(s,i);
			i++;
		}		
	}
	
	public void load(String fn,int idx) {
		MList fu=new MList();
		fu.load(fn);
		String s;
		int i=0;
		while((s=fu.getNext())!=null) {
			double r=process_rs(s);
//			Log.prn(1,s+" "+r);
			g_rs[i][idx]=r;
			i++;
		}
	}
	
	public void save(String fn) {
		MOut fu=new MOut(g_path+"/"+fn);
		String str="xx";

		for(int idx=0;idx<g_max;idx++) {
			// TODO algo name 
			str+=" test";
		}
		fu.write(str);
		for(int i=0;i<g_xlen;i++) {
			str=g_xl.elementAt(i);
			for(int idx=0;idx<g_max;idx++) {
				str+=" "+g_rs[i][idx];
			}
			fu.write(str);
//			Log.prn(1, str);
		}
		fu.save();
		
	}
	private double process_rs(String rs) {
		MList fu=new MList();
		fu.load(rs);
		int n=fu.size();
		int p=0;
		for(int i=0;i<n;i++) {
			String s=fu.get(i);
			if(s.equals("1"))
				p++;
		}
		return (p*1.0/n);
	}
	
	
}