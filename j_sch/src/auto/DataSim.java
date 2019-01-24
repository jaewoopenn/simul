package auto;

import java.util.Vector;

import anal.Anal;
import anal.AnalSel;
import util.FOut;
import util.FUtilSp;

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
		FUtilSp fu=new FUtilSp(g_path+"/"+fn);
		fu.load();
		g_xlen=fu.size();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			g_xl.add(s);
		}
		g_rs=new double[10][g_xlen];
	}
	public void load_rs(String fn){
		FUtilSp fu=new FUtilSp(g_path+"/"+fn);
		fu.load();
		int size=fu.size();
		g_max=size;
		for(int i=0;i<size;i++) {
			String s=fu.get(i);
			load(s,i);
		}		
	}
	
	public void load(String fn,int idx) {
		FUtilSp fu=new FUtilSp(fn);
		fu.load();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			double r=process_rs(s);
//			Log.prn(1,s+" "+r);
			g_rs[i][idx]=r;
		}
	}
	
	public void save(String fn) {
		FOut fu=new FOut(g_path+"/"+fn);
		String str="xx";

		for(int idx=0;idx<g_max;idx++) {
			Anal a=AnalSel.getAnal(idx);
			str+=" "+a.getName();
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
		FUtilSp fu=new FUtilSp(rs);
		fu.load();
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
