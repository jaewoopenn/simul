package auto;

import anal.Anal;
import anal.AnalSel;
import basic.TaskMng;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenMC;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SimulSel;
import sim.SysMng;
import sim.TaskSimul;
import util.FOut;
import util.FUtilSp;
import util.S_Log;

public class Platform {
	private String g_path;
	private int g_num=100;
	private int g_dur=2000;
	private double g_prob=0.3;
	private boolean g_isCheck=false;
	public Platform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}
	public void setDur(int n) {
		g_dur=n;
	}
	public void setProb(double d) {
		g_prob=d;
	}
	public void setCheck(){
		g_isCheck=true;
	}
	
	// gen CFG, TS
	public void genCfg_util(String cf,int end) {
		int base=50;
		int step=5;
		int end_i=(end-50)/step;
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
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGenMC(cfg);
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
		FUtilSp fu=new FUtilSp(g_path+"/"+ts_list);
		String rs_fn=g_path+"/a_rs_list."+sort+".txt";
		FOut fu_rs=new FOut(rs_fn);
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
		FOut fu=new FOut(out);
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;

			a.init(tm);
			a.prepare();
			if(a.is_sch()) {
				fu.write("1");
			} else {
				fu.write("0");
			}
		}
		fu.save();
		
	}
	
	//simulation
	public void sim_loop(String rs_list,String ts_list, int end) {
		FOut fu=new FOut(g_path+"/"+rs_list);
		for(int i=0;i<end;i++){
			String rs=simul(ts_list,i);
			fu.write(rs);
		}
		fu.save();
	}

	// simulate task set list with algorithm choice
	public String simul(String ts_list,int sort) {
		FUtilSp fu=new FUtilSp(g_path+ts_list);
		fu.load();
		String rs_fn=g_path+"a_sim_list."+sort+".txt";
		FOut fu_rs=new FOut(rs_fn);
		
		Anal a=AnalSel.getAnal(sort);
		TaskSimul s=SimulSel.getSim(sort);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".sim."+sort;
			simul_one(fn,out,a,s);
			fu_rs.write(out);
		}		
		fu_rs.save();
		return rs_fn;		
	}

	public void simul_one(String ts,String out,Anal a,TaskSimul s) {
		S_Log.prn(2, ts);
		SysLoad sy=new SysLoad(ts);
		sy.open();
		int n=0;
		FOut fu=new FOut(out);
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			
			a.init(tm);
			a.prepare();
			n++;
			S_Log.prn(2, "n:"+n);
			if(!a.is_sch()) {
				S_Log.prn(2, "no sch");
				continue;
			}

			double x=a.computeX();
			SysMng sm=new SysMng();
			sm.setMS_Prob(g_prob);
			sm.setX(x);
//			sm.prn();
			s.init_sm_tm(sm,tm);
			s.simul(0,g_dur);
			s.simul_end();
			SimulInfo si=s.getSI();
			fu.write(si.getDMR()+"");
		}
		fu.save();
	}


	
	
}
