package auto;

import anal.Anal;
import anal.AnalSel;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenMC;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SimulSel;
import sim.SysMng;
import sim.TaskSimul;
import task.TaskMng;
import util.MList;
import util.CProg;
import util.SLog;

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
	public void genCfg_util(String cf,double ul) {
		int base=50;
		int step=5;
		double end_i=(ul*100-50)/step;
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			SLog.prn(2, lb+"");
			cg.setParam("u_lb", (lb)*1.0/100+"");
			cg.setParam("u_ub", (lb+5)*1.0/100+"");
			cg.setParam("mod", (lb+5)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.add(fn);
		}
		fu.save(g_path+"/"+cf);
		
	}

	public void genTS(String cfg_list,String ts, String xaxis) {
		SLog.prn(3, g_path+"/"+cfg_list);
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_ts=new MList();
		MList fu_xa=new MList();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGenMC(cfg);
			String fn=cfg.get_fn();
			if(g_isCheck)
				sg.setCheck();
			sg.gen(fn);
			fu_ts.add(fn);
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_ts.save(g_path+"/"+ts);
		fu_xa.save(g_path+"/"+xaxis);
	}
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_path+"/"+rs_list);
	}
	
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_path+"/a_rs_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=AnalSel.getAnal(sort);
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".rs."+sort;
			anal_one(fn,out,a);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;
	}
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;

			a.init(tm);
			a.prepare();
			if(a.is_sch()) {
				fu.add("1");
			} else {
				fu.add("0");
			}
		}
		fu.save(out);
		
	}
	
	//simulation
	public void sim_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=simul(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_path+"/"+rs_list);
	}

	// simulate task set list with algorithm choice
	public String simul(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_path+"a_sim_list."+sort+".txt";
		MList fu_rs=new MList();
		
		Anal a=AnalSel.getAnal(sort);
		TaskSimul s=SimulSel.getSim(sort);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".sim."+sort;
			simul_one(fn,out,a,s);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;		
	}

	public void simul_one(String ts,String out,Anal a,TaskSimul s) {
		SLog.prn(2, ts);
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		CProg prog=new CProg(num);
		prog.setLog(2);
		prog.setPercent();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			
			a.init(tm);
			a.prepare();
			prog.inc();
			if(!a.is_sch()) {
				SLog.prn(2, "no sch");
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
			fu.add(si.getDMR()+"");
		}
		fu.save(out);
	}


	
	
}
