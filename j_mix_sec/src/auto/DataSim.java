package auto;

import java.util.Vector;

import anal.Anal;
import util.MList;

public class DataSim {
	private String g_path;
	private Vector<String> g_xl=new Vector<String>();
	private double[][] g_rs;
	private int g_max=0;
	private int g_xlen=0;
	public DataSim(String path,int x) {
		g_path=path;
		g_max=x;
	}
	public void load_x(String fn) {
		MList fu=new MList(g_path+"/"+fn);
		g_xlen=fu.size();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			g_xl.add(s);
		}
		g_rs=new double[10][g_xlen];
	}
	public void load_rs(String fn){
		MList fu=new MList(g_path+"/"+fn);
		int size=fu.size();
		g_max=size;
		for(int i=0;i<size;i++) {
			String s=fu.get(i);
			load(s,i);
		}		
	}
	
	public void load(String fn,int idx) {
		MList fu=new MList(fn);
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			double r=process_rs(s);
//			Log.prn(1,s+" "+r);
			g_rs[i][idx]=r;
		}
	}
	

	private double process_rs(String rs) {
		MList fu=new MList(rs);
		int n=fu.size();
		double r=0;
		for(int i=0;i<n;i++) {
			String s=fu.get(i);
			r+=Double.valueOf(s);
		}
		n=Math.max(1,n);
		double rst=(r/n);
//		Log.prn(1, rst+"");
		return rst;
	}
	
}
