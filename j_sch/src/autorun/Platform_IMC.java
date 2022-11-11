package autorun;

import anal.Anal;
import auto.Platform_base;
import gen.ConfigGen;
import gen.SysGen;
import imc.AnalEDF_VD_IMC;
import imc.AnalSel_IMC;
import imc.SimulSel_IMC;
import sim.TaskSimul_base;
import util.MList;
import util.SLog;

public class Platform_IMC extends Platform_base {
	private boolean g_onlyMC=false;
	
	public Platform_IMC(String path, String rs_path) {
		g_path=path;
		g_rs_path=rs_path;
	}	

	// gen CFG, TS
	public void genCfg_util(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			SLog.prn(2, lb+"");
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
		fu.save(g_path+"/"+cf);
		
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
			String fn=cfg.get_fn();
			if(g_onlyMC)
				sg.setOnlyMC();
			if(g_isCheck)
				sg.setCheck();
			int num=sg.prepareIMC();
			sg.gen(g_path+"/"+fn, a,num);
			fu_ts.add(fn);
		}
		fu_ts.save(g_path+"/"+ts);
	}
	

	
	public Anal getAnal(int sort) {
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
	public void setLife(int i) {
		g_life=i;
		
	}

	
}