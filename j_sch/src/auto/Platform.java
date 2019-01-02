package auto;

import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import gen.SysLoad;
import util.FUtil;
import util.FUtilSp;
import util.Log;

public class Platform {
	private String g_path;
	private int g_num=100;
	public Platform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}

	public void genUtil(String cf) {
		ConfigGen eg=ConfigGen.getPredefined();
		FUtil fu=new FUtil(g_path+cf);
		eg.setParam("subfix", g_path);
		eg.setParam("num",g_num+"");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			Log.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			String fn=g_path+"cfg_"+i+".txt";
			eg.setFile(fn);
			eg.write();
			fu.write(fn);
		}
		fu.save();
		
	}

	public void genTS(String cfg_list) {
		FUtilSp fu=new FUtilSp(g_path+cfg_list);
		
		FUtil fu_ts=new FUtil(g_path+"a_ts_list.txt");
		FUtil fu_rs=new FUtil(g_path+"a_x_list.txt");
		fu.load();
//		int n=fu.load();
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
	public void anal_loop(String rs_list,String ts_list, int end) {
		FUtil fu=new FUtil(g_path+rs_list);
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.write(rs);
		}
		fu.save();
		
	}
	
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		FUtilSp fu=new FUtilSp(g_path+ts_list);
		String rs_fn=g_path+"a_rs_list."+sort+".txt";
		FUtil fu_rs=new FUtil(rs_fn);
		fu.load();
		Anal a=AnalSel.getAnal(sort);
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".rs."+sort;
			anal_one(fn,out,a);
			fu_rs.write(out);
		}		
		fu_rs.save();
		return rs_fn;
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
	// simulate task set list with algorithm choice
	
	public void simul(String ts_list,int sort) {
		
	}


	
	
}
