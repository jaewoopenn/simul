package auto;

import anal.Anal;
import anal.AnalEDF_VD_IMC;
import anal.AnalSel_IMC;
import anal.AnalSel_MC;
import gen.ConfigGen;
import gen.SysGen;
import imc.SimulSel_IMC;
import sim.TaskSimul_base;
import util.MList;
import util.SLog;

public class Platform_IMC extends Platform_base {
	private boolean g_onlyMC=false;
	private boolean g_isMC=false; // not IMC
	private int g_stage=1;
	
	public Platform_IMC(String path) {
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
	public Anal getAnalSim(int sort) {
		return AnalSel_IMC.getAnalSim(sort);
	}
	public TaskSimul_base getSim(int sort) {
		return SimulSel_IMC.getSim(sort);
	}
	public void setOnlyMC() {
		g_onlyMC=true;		
	}

	public void setStage(int s) {
		g_stage=s;
	}




	
}
