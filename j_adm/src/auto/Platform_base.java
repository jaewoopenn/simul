package auto;

import anal.Anal;
import anal.AnalEDF_VD_IMC;
import anal.AnalSel_IMC;
import anal.AnalSel_MC;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysLoad;
import imc.SimulSel_IMC;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul_base;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.CProg;
import util.MCal;
import util.MList;
import util.SLog;

public  class Platform_base {
	private boolean g_onlyMC=false;
	private boolean g_isMC=false; // not IMC
	private int g_stage=1;
	private String g_path;
	private String g_rs_path;
	private int g_num=100;
	private int g_dur=2000;
	private double g_p_ms=0.3;
	private double g_p_hc=0.5;
	private double g_ratio=-1;
	private double g_ratio_hi=-1;
	private boolean g_isSch=false;
	private int g_dur_set[]= {4000,8000,16000,32000,64000,128000};
	private boolean g_verbose=false;

	public Platform_base(String path) {
		g_path=path;
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}

	public void setMC() {
		g_isMC=true;
	}
	// gen CFG, TS
	public void genCfg_util(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg;
		if(g_isMC)
			cg=ConfigGen.getPredefinedMC();
		else
			cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			cg.setParam("u_lb", (lb)*1.0/100+"");
			cg.setParam("u_ub", (lb+step)*1.0/100+"");
			cg.setParam("mod", (lb+step)+"");
			cg.setParam("prob_hi",g_p_hc+"");
			if(g_ratio!=-1) {
				cg.setParam("r_lb",(g_ratio)+"");
				cg.setParam("r_ub",(g_ratio_hi)+"");
			}
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}

	
	// gen 
	public void genCfg(String cf) {
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		cg.setParam("u_lb", 0.85+"");
		cg.setParam("u_ub", 0.90+"");
		cg.setParam("mod", 0+"");
		cg.setParam("prob_hi",g_p_hc+"");
		String fn=g_path+"/cfg_0.txt";
		cg.setFile(fn);
		cg.write();
		fu.add(fn);
		fu.saveTo(g_path+"/"+cf);		
	}
	
	// gen mo
	public void genCfg_mo(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		for(int i=0;i<=end_i;i++){
			int lb=i*step+base;
			SLog.prn(2, lb+"");
//			cg.setParam("u_lb", 0.90+"");
//			cg.setParam("u_ub", 0.95+"");
			cg.setParam("u_lb", 0.85+"");
			cg.setParam("u_ub", 0.90+"");
//			cg.setParam("u_lb", 0.80+"");
//			cg.setParam("u_ub", 0.85+"");
			cg.setParam("mo_lb", (lb)*1.0/100+"");
			cg.setParam("mo_ub", (lb+step)*1.0/100+"");
			cg.setParam("mod", (lb)+"");
			cg.setParam("prob_hi",g_p_hc+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}
	

	
	
	public void genTS(String cfg_list,String ts) {
		SLog.prn(3, g_path+"/"+cfg_list);
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_ts=new MList();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		int max=fu.size();
		Anal a=new AnalEDF_VD_IMC();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGen(cfg);
			sg.setStage(g_stage);
			String fn=cfg.get_fn();
			SLog.prn(3, fn);
			if(g_onlyMC)
				sg.setOnlyMC();
			if(g_isSch)
				sg.setSch();
			int num=sg.prepare_IMC();
			sg.gen2(g_path+"/"+fn, a,num);
			fu_ts.add(fn);
		}
		fu_ts.saveTo(g_path+"/"+ts);
	}
	

	
	public Anal getAnal(int sort) {
		if(g_isMC)
			return AnalSel_MC.getAnal(sort);
		else
			return AnalSel_IMC.getAnal(sort);
	}
	public void setOnlyMC() {
		g_onlyMC=true;		
	}

	public void setStage(int s) {
		g_stage=s;
	}



	
	public void setNum(int n) {
		g_num=n;
	}
	public void setDur(int n) {
		g_dur=n;
	}
	public void setP_MS(double d) {
		g_p_ms=d;
	}
	public void setP_HC(double d) {
		g_p_hc=d;
	}
	public void setRatio(double d) {
		g_ratio=d;
	}
	public void setRatio_hi(double d) {
		g_ratio_hi=d;
		
	}
	public void setSch(){
		g_isSch=true;
	}
	
	public void setVerbose(){
		g_verbose=true;
	}

	
	public void genXA(String cfg_list, String xaxis) {
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_xa=new MList();
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_xa.saveTo(g_rs_path+"/"+xaxis);
	}
	
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+rs_list);
	}
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_rs_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=getAnal(sort);
		
		SLog.prn(1, "a:"+a.getName());
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=g_rs_path+"/"+fn+".rs."+sort;
//			SLog.prn(2, out);
			anal_one(fn,out,a);
			fu_rs.add(out);
		}		
		fu_rs.saveTo(rs_fn);
		return rs_fn;
	}
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(g_path+"/"+ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			DTaskVec dt=sy.loadOne2();
			if(dt==null) break;
			double dtm=0;
//			SLog.prn(2, i+"");
			a.reset();
			for(int j=0;j<dt.getNum();j++) {
				TaskSet tmp=new TaskSet(dt.getVec(j));
				TaskMng tm=tmp.getTM();
				a.init(tm);
				dtm=Math.max(dtm, a.getDtm());
			}
			if(dtm<=1+MCal.err) {
				fu.add("1");
			} else {
				fu.add("0");
			}
		}
		fu.saveTo(out);
	}
	
	


}




//public void gen_scn(String ts_list,int ts_list_n, int ts_n, int scn_n) {
//MList fu=new MList(g_path+"/"+ts_list);
//
//String fn=fu.get(ts_list_n);
//for(int i=0;i<scn_n;i++) {
//	String out=g_rs_path+"/"+fn+".sim."+i+".txt";
//	SLog.prn(2,out);
//	gen_scn_one(g_path+"/"+fn,out,ts_n);
//}
//}
//
//public String run_scn(int sort,String ts_list,int ts_list_n,int ts_n,int scn_from, int scn_to) {
//MList fu=new MList(g_path+"/"+ts_list);
//String rs_fn=g_rs_path+"/a_sim_list."+sort+".txt";
//Anal a=getAnalSim(sort);
//SLog.prn(2, "Anal:"+a.getName());
//TaskSimul_base s=getSim(sort);
//s.setRecoverIdle(g_recoverIdle);
//
//String fn=fu.get(ts_list_n);
//for(int i=scn_from;i<scn_to;i++) {
//	String out=g_rs_path+"/"+fn+".sim."+i+".txt";
//	SLog.prn(2,out);
//	run_scn_one(g_path+"/"+fn,out,a,s,ts_n,i);
//}
//return rs_fn;		
//}

//public void run_scn_one(String ts,String out,Anal a,TaskSimul_base s,int ts_n, int scn) {
//	SysLoad sy=new SysLoad(ts);
//	sy.open();
//	Anal base;
//	MList fu=new MList();
//	DTaskVec dt=null;
//	for(int i=0;i<=ts_n;i++) {
//		dt=sy.loadOne2();
//		if(i<ts_n)
//			continue;
//	}
//	a.init(dt.getTM(0));
//	if(!a.is_sch()) {
//		SLog.prn(2, "no sch "+ts_n);
//		fu.add("1.0");
//		return;
//	}
//	base=new AnalEDF_IMC();
//	base.init(dt.getTM(0));
//	if(base.is_sch()) {
//		SLog.prn(2, "EDF sch "+ts_n);
//		fu.add("0.0");
//		return;
//	}
//
//	double x=a.computeX();
//	
//	SysMng sm=new SysMng();
//	sm.setMS_Prob(g_p_ms);
//	sm.setX(x);
//	s.init_sm_dt(sm,dt);
//	s.simul(g_dur);
//}


//public void gen_scn_one(String ts,String out,int ts_n) {
////	SLog.prn(2, ts);
//	SysLoad sy=new SysLoad(ts);
//	sy.open();
//	SLogF.init(out);
//	SLogF.setGen();
//	sy.moveto(ts_n);
//	DTaskVec dt=sy.loadOne2();
//	SysMng sm=new SysMng();
//	TaskSimul_base s=new TaskSimul_EDF_IMC_gen();
//	sm.setMS_Prob(g_p_ms);
//	sm.setX(1);
//	s.init_sm_dt(sm,dt);
//	s.simul(g_dur);
//	SLogF.save();
//	
//}
