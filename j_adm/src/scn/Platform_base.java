package scn;

import anal.Anal;
import anal.AnalEDF_VD_IMC;
import gen.ConfigGen;
import gen.SysGen;
import util.MList;
import util.SLog;

public  class Platform_base {
	private boolean g_onlyMC=false;
	private int g_stage=1;
	private String g_path;
	private String g_rs_path;
	private boolean g_isSch=false;

	public Platform_base(String path) {
		g_path=path;
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}

	
	
	public void genTS(String cfg_list,String ts) {
		SLog.prn(3, g_path+"/"+cfg_list);
		MList fu=MList.load(g_path+"/"+cfg_list);
		
		MList fu_ts=MList.new_list();
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
	

	
	public void setOnlyMC() {
		g_onlyMC=true;		
	}

	public void setStage(int s) {
		g_stage=s;
	}



	
	public void setSch(){
		g_isSch=true;
	}
	

	
	public void genXA(String cfg_list, String xaxis) {
		MList fu=MList.load(g_path+"/"+cfg_list);
		
		MList fu_xa=MList.new_list();
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_xa.saveTo(g_rs_path+"/"+xaxis);
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
