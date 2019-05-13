package auto;

import gen.ConfigGen;
import gen.SysGen;
import util.FOut;
import util.FUtilSp;
import util.S_Log;

public class Platform {
	private String g_path;
	private int g_num=100;
	private boolean g_isCheck=false;
	public Platform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}
	public void setCheck(){
		g_isCheck=true;
	}
	
	// gen CFG, TS
	public void genCfg_util(String cf,double ul) {
		int base=50;
		int step=5;
		double end_i=(ul*100-50)/step;
		ConfigGen cg=ConfigGen.getPredefined();
		FOut fu=new FOut(g_path+"/"+cf);
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			S_Log.prn(2, lb+"");
			cg.setParam("u_lb", (lb)*1.0/100+"");
			cg.setParam("u_ub", (lb+5)*1.0/100+"");
			cg.setParam("mod", (lb+5)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.write(fn);
		}
		fu.save();
		
	}

	public void genTS(String cfg_list,String ts, String xaxis) {
		FUtilSp fu=new FUtilSp(g_path+"/"+cfg_list);
		
		FOut fu_ts=new FOut(g_path+"/"+ts);
		FOut fu_xa=new FOut(g_path+"/"+xaxis);
		fu.load();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGen(cfg);
			String fn=cfg.get_fn();
			if(g_isCheck)
				sg.setCheck();
			sg.gen(fn);
			fu_ts.write(fn);
			String mod=cfg.get_mod();
			fu_xa.write(mod);
		}
		fu_ts.save();
		fu_xa.save();
	}
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		FOut fu=new FOut(g_path+"/"+rs_list);
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.write(rs);
		}
		fu.save();
	}
	
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		return null;
	}
	
//	public void anal_one(String ts,String out,Anal a) {
//		SysLoad sy=new SysLoad(ts);
//		String ret=sy.open();
//		int num=Integer.valueOf(ret).intValue();
//		FOut fu=new FOut(out);
//		for(int i=0;i<num;i++) {
//			TaskMng tm=sy.loadOne();
//			if(tm==null) break;
//
//			a.init(tm);
//			a.prepare();
//			if(a.is_sch()) {
//				fu.write("1");
//			} else {
//				fu.write("0");
//			}
//		}
//		fu.save();
//		
//	}
	
	
	
}
