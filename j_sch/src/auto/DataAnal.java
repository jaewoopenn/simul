package auto;

import anal.Anal;
import anal.AnalEDF;
import anal.AnalEDF_VD;
import basic.TaskMng;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import gen.SysLoad;
import util.FUtil;
import util.Log;

public class DataAnal {
	private String g_path;
	
	public DataAnal(String path) {
		g_path=path;
	}
	public void load_x(String fn) {
		FUtil fu=new FUtil(g_path+fn);
		fu.load();
		for(int i=0;i<fu.size();i++) {
			String s=fu.get(i);
			Log.prn(1,s);
		}
	}

	public void gen(String cfg_list) {
		FUtil fu=new FUtil(g_path+cfg_list);
		
		FUtil fu_ts=new FUtil(g_path+"a_ts_list.txt");
		FUtil fu_rs=new FUtil(g_path+"a_x_list.txt");
		int n=fu.load();
//		Log.prn(1, n+" ");
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen eg=new SysGenTM(cfg);
			String fn=eg.get_fn();
			eg.gen(fn);
			fu_ts.write(fn);
			String mod=eg.get_mod();
			fu_rs.write(mod);
		}
		fu_ts.save();
		fu_rs.save();
	}
	private Anal getAnal(int sort) {
		if(sort==0) {
			return new AnalEDF();
		} else if(sort==1) {
			return new AnalEDF_VD();
		}
		return null;
	}
	public void anal(String ts_list,int sort) {
		FUtil fu=new FUtil(g_path+ts_list);
		
		FUtil fu_rs=new FUtil(g_path+"a_rs_list."+sort+".txt");
		fu.load();
		Anal a=getAnal(sort);
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".rs."+sort;
			anal_one(fn,out,a);
			fu_rs.write(out);
		}		
		fu_rs.save();
	}
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(ts);
		sy.open();
		FUtil fu=new FUtil(out);
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			a.init(tm);
			a.prepare();
			if(a.isScheduable()) {
				fu.write("1");
			} else {
				fu.write("0");
			}
			fu.save();
		}
		
	}
	
	
}
