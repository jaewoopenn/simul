package auto;

import gen.ConfigGen;
import gen.ConfigPre;
import util.MList;
import util.SLog;

public  class AutoConfig {
	private String g_path;
	private AutoParConfig g_apc;
	public AutoConfig(String path,AutoParConfig apc) {
		g_path=path;
		g_apc=apc;
	}	

	// gen CFG, TS
	public void genCfg_util(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg;
		cg=ConfigPre.getPredefined(g_apc.isMC);
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_apc.num+"");
		cg.setParam("prob_hi",g_apc.p_hc+"");
		if(g_apc.ratio!=-1) {
			cg.setParam("r_lb",(g_apc.ratio)+"");
			cg.setParam("r_ub",(g_apc.ratio_hi)+"");
		}
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			cg.setParam("u_lb", (lb)*1.0/100+"");
			cg.setParam("u_ub", (lb+step)*1.0/100+"");
			cg.setParam("mod", (lb+step)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}

	
	// gen 
	public void genCfg(String cf) {
		ConfigGen cg=ConfigPre.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_apc.num+"");
		cg.setParam("u_lb", 0.85+"");
		cg.setParam("u_ub", 0.90+"");
		cg.setParam("mod", 0+"");
		cg.setParam("prob_hi",g_apc.p_hc+"");
		String fn=g_path+"/cfg_0.txt";
		cg.setFile(fn);
		cg.write();
		fu.add(fn);
		fu.saveTo(g_path+"/"+cf);		
	}
	
	// gen mo
	public void genCfg_mo(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg=ConfigPre.getPredefined();
		MList fu=new MList();
		cg.setParam("subfix", g_path);
		cg.setParam("num",g_apc.num+"");
		cg.setParam("u_lb", 0.85+"");
		cg.setParam("u_ub", 0.90+"");
		cg.setParam("prob_hi",g_apc.p_hc+"");
		for(int i=0;i<=end_i;i++){
			int lb=i*step+base;
			SLog.prn(2, lb+"");
			cg.setParam("mo_lb", (lb)*1.0/100+"");
			cg.setParam("mo_ub", (lb+step)*1.0/100+"");
			cg.setParam("mod", (lb)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.setFile(fn);
			cg.write();
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}
	

	
	
	

	



	

	
	
	
}
