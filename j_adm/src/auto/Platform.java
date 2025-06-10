package auto;

import anal.Anal;
import anal.AnalEDF_VD;
import anal.AnalSel;
import gen.ConfigGen;
import gen.SysGen;
import sim.SimulSel;
import sim.TaskSimul_base;
import util.MList;
import util.SLog;

public class Platform extends Platform_base{
	
	public Platform(String path) {
		g_path=path;
		g_rs_path=path;
	}
	public Platform(String path, String rs_path) {
		g_path=path;
		g_rs_path=rs_path;
	}	

	public void setRecoverIdle(boolean b) {
		g_recoverIdle=b;
	}
	
	// gen CFG, TS
	public void genCfg_util(String cf,int base,int step, int end)  {
		double end_i=(end-base)/step;
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
//		cg.setParam("subfix", g_path);
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
		fu.saveTo(g_path+"/"+cf);
		
	}
	
	public void genCfg_util_one(String cf,double util) {
		ConfigGen cg=ConfigGen.getPredefined();
		MList fu=new MList();
//		cg.setParam("subfix", g_path);
		cg.setParam("num",g_num+"");
		cg.setParam("u_lb", (util)+"");
		cg.setParam("u_ub", (util+0.05)+"");
		cg.setParam("mod", "0");
		cg.setParam("prob_hi",g_p_hc+"");
		String fn=g_path+"/cfg_0.txt";
		cg.setFile(fn);
		cg.write();
		fu.add(fn);
		fu.saveTo(g_path+"/"+cf);
		
	}
	
	public void genCfg_hc(String cf,double util) {
//		ConfigGen cg=ConfigGen.getPredefined();
//		MList fu=new MList();
//		cg.setParam("subfix", g_path);
//		cg.setParam("num",g_num+"");
//		for(int i=0;i<10;i++){
//			int p_hc=i+1;
//			cg.setParam("u_lb", (util)+"");
//			cg.setParam("u_ub", (util+0.05)+"");
//			cg.setParam("mod", "0."+(p_hc));
//			cg.setParam("prob_hi",(p_hc*0.1)+"");
//			String fn=g_path+"/cfg_"+i+".txt";
//			cg.setFile(fn);
//			cg.write();
//			fu.add(fn);
//		}
//		fu.save(g_path+"/"+cf);
		
	}
	
	public void genCfg_ratio(String cf,double util) {

		
	}

	
	public void genTS(String cfg_list,String ts) {
		SLog.prn(3, g_path+"/"+cfg_list);
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_ts=new MList();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		int max=fu.size();
//		Anal a=new AnalFMC();
		Anal a=new AnalEDF_VD();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGen(cfg);
			String fn=cfg.get_fn();
			if(g_isCheck)
				sg.setCheck();
			int num=sg.prepare();
			sg.gen(g_path+"/"+fn, a,num);
			fu_ts.add(fn);
		}
		fu_ts.saveTo(g_path+"/"+ts);
	}
	

//	public int getDur(int i) {
//		return i*3000+2000;
//	}

	
	public Anal getAnal(int sort) {
		return AnalSel.getAnal(sort);
	}
	public Anal getAnalSim(int sort) {
		return AnalSel.getAnalSim(sort);
	}


	
	public TaskSimul_base getSim(int sort) {
		return SimulSel.getSim(sort);
	}
	
}
