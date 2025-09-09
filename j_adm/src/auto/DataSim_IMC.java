package auto;

import java.util.Vector;

import sim.SimulSel_IMC;
import sim.TaskSimul;
import util.MList;

public class DataSim_IMC {
	private String g_path;
	private Vector<String> g_xl=new Vector<String>();
	private double[][] g_rs;
	private int g_max=0;
	private int g_xlen=0;
	public DataSim_IMC(String path,int x) {
		g_path=path;
		g_max=x;
	}
	public void load_x(String fn) {
		MList fu=MList.load(g_path+"/"+fn);
		g_xlen=fu.size();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			g_xl.add(s);
		}
		g_rs=new double[11][g_xlen];
	}
	public void load_rs(String fn){
		MList fu=MList.load(g_path+"/"+fn);
		int size=fu.size();
		g_max=size;
		for(int i=0;i<size;i++) {
			String s=fu.get(i);
			load(s,i);
		}		
	}
	
	public void load(String fn,int idx) {
		MList fu=MList.load(fn);
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			double r=process_rs(s);
//			Log.prn(1,s+" "+r);
			g_rs[i][idx]=r;
		}
	}

	private double process_rs(String rs) {
		MList fu=MList.load(rs);
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
	public void saveSim(String fn) {
		MList fu=MList.new_list();
		String str="xx";

		for(int idx=0;idx<g_max;idx++) {
			TaskSimul a=SimulSel_IMC.getSim(idx);
			str+=" "+a.getName();
		}
		fu.add(str);
		for(int i=0;i<g_xlen;i++) {
			str=g_xl.elementAt(i);
			for(int idx=0;idx<g_max;idx++) {
				str+=" "+g_rs[i][idx];
			}
			fu.add(str);
//			Log.prn(1, str);
		}
		fu.saveTo(g_path+"/"+fn);
		
	}
	
	
}
