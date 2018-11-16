package auto;

import java.util.Vector;

import util.FUtil;

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
		FUtil fu=new FUtil(g_path+fn);
		fu.load();
		g_xlen=fu.size();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			g_xl.add(s);
		}
		g_rs=new double[10][g_xlen];
	}

	public void load_rs(String fn,int idx) {
		FUtil fu=new FUtil(g_path+fn);
		fu.load();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			double r=process_rs(s);
//			Log.prn(1,s+" "+r);
			g_rs[i][idx]=r;
		}
	}
	
	public void save(String fn) {
		FUtil fu=new FUtil(g_path+fn);
		for(int i=0;i<g_xlen;i++) {
			String str=g_xl.elementAt(i);
			for(int idx=0;idx<g_max;idx++) {
				str+=" "+g_rs[i][idx];
			}
			fu.write(str);
//			Log.prn(1, str);
		}
		fu.save();
		
	}
	private double process_rs(String rs) {
		FUtil fu=new FUtil(rs);
		fu.load();
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
