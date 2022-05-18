package autorun;

import anal.Anal;
import anal.AnalEDF_VD;
import auto.Platform_base;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenMC;
import imc.AnalSel_IMC;
import imc.AnalSel_run;
import imc.SimulSel_IMC;
import imc.TaskSimulIMC;
import sim.mc.TaskSimulMC;
import util.MList;
import util.SLog;

public class Platform_IMC extends Platform_base {
	public Platform_IMC(String path) {
		g_path=path;
	}	// gen CFG, TS
	public void genCfg_util(String cf,double ul) {
		int base=60;
		int step=4;
		double end_i=(ul*100-base)/step;
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
	public void genCfg_util_one(String cf,double util) {
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		cg.setParam("u_lb", (util)+"");
		cg.setParam("u_ub", (util+0.05)+"");
		cg.setParam("mod", "0");
		cg.setParam("prob_hi",g_p_hc+"");
		String fn=g_path+"/cfg_0.txt";
		cg.setFile(fn);
		cg.write();
		fu.add(fn);
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
		Anal a=new AnalEDF_VD();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGenMC(cfg);
			String fn=cfg.get_fn();
			if(g_isCheck)
				sg.setCheck();
			sg.gen(fn, a);
			fu_ts.add(fn);
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_ts.save(g_path+"/"+ts);
		fu_xa.save(g_path+"/"+xaxis);
	}
	
	public void genXA(String xaxis) {
		MList fu_xa=new MList();
		for(int i=0;i<g_dur_set.length;i++) {
			fu_xa.add((g_dur_set[i]/1000)+"");
		}
		fu_xa.save(g_path+"/"+xaxis);
	}	
	
	
	public Anal getAnal(int sort) {
		return AnalSel_IMC.getAnal(sort);
	}
	public Anal getAnalSim(int sort) {
		return AnalSel_IMC.getAnalSim(sort);
	}
	public TaskSimulMC getSim(int sort) {
		return SimulSel_IMC.getSim(sort);
	}


	
}
