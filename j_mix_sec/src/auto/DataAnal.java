package auto;

import java.util.Vector;

import anal.Anal;
import anal.AnalSel;
import util.MList;
import util.SLog;

public class DataAnal {
	protected String g_path;
	protected Vector<String> g_xl=new Vector<String>();
	protected double[][] g_rs;
	protected int g_max=0;
	protected int g_xlen=0;
	public DataAnal(String path,int x) {
		g_path=path;
		g_max=x;
	}
	public void load_x(String fn) {
		MList fu=new MList(g_path+"/"+fn);
		g_xlen=fu.size();
		for(int i=0;i<g_xlen;i++) {
			String s=fu.get(i);
			g_xl.add(s);
		}
		g_rs=new double[20][g_xlen];
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
//			SLog.prn(2,s+" "+r);
			g_rs[i][idx]=r;
		}
	}
	
	public void save(String fn) {
		MList fu=new MList();
		String str="xx";

		for(int idx=0;idx<g_max;idx++) {
			Anal a=getAnal(idx);
			str+=" "+a.getName();
		}
		fu.add(str);
		for(int i=0;i<g_xlen;i++) {
			str=g_xl.elementAt(i);
			for(int idx=0;idx<g_max;idx++) {
				str+=" "+g_rs[i][idx];
			}
			fu.add(str);
		}
		fu.save(g_path+"/"+fn);
	}
	public Anal getAnal(int i) {
		return AnalSel.getAnal(i);
	}


	
	private double process_rs(String rs) {
		MList fu=new MList(rs);
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
