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
		cg=ConfigPre.getPredefined(g_apc.getMC());
		MList fu=MList.new_list();
		cg.setPar("subfix", g_path);
		cg.setPar("num",g_apc.num+"");
		cg.setPar("prob_hi",g_apc.p_hc+"");
		if(g_apc.ratio!=-1) {
			cg.setPar("r_lb",(g_apc.ratio)+"");
			cg.setPar("r_ub",(g_apc.ratio_hi)+"");
		}
		for(int i=0;i<end_i;i++){
			int lb=i*step+base;
			cg.setPar("u_lb", (lb)*1.0/100+"");
			cg.setPar("u_ub", (lb+step)*1.0/100+"");
			cg.setPar("mod", (lb+step)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.write(fn);
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}

	
	// gen 
	public void genCfg(String cf) {
		ConfigGen cg=ConfigPre.getPredefined();
		MList fu=MList.new_list();
		cg.setPar("subfix", g_path);
		cg.setPar("num",g_apc.num+"");
		cg.setPar("u_lb", 0.85+"");
		cg.setPar("u_ub", 0.90+"");
		cg.setPar("mod", 0+"");
		cg.setPar("prob_hi",g_apc.p_hc+"");
		String fn=g_path+"/cfg_0.txt";
		cg.write(fn);
		fu.add(fn);
		fu.saveTo(g_path+"/"+cf);		
	}
	
	// gen mo
	public void genCfg_mo(String cf,int base,int step, int end) {
		double end_i=(end-base)/step;
		ConfigGen cg=ConfigPre.getPredefined();
		MList fu=MList.new_list();
		cg.setPar("subfix", g_path);
		cg.setPar("num",g_apc.num+"");
		cg.setPar("u_lb", 0.85+"");
		cg.setPar("u_ub", 0.90+"");
		cg.setPar("prob_hi",g_apc.p_hc+"");
		for(int i=0;i<=end_i;i++){
			int lb=i*step+base;
			SLog.prn(2, lb+"");
			cg.setPar("mo_lb", (lb)*1.0/100+"");
			cg.setPar("mo_ub", (lb+step)*1.0/100+"");
			cg.setPar("mod", (lb)+"");
			String fn=g_path+"/cfg_"+i+".txt";
			cg.write(fn);
			fu.add(fn);
		}
		fu.saveTo(g_path+"/"+cf);
	}
	

	
	
	

	



	

	
	
	
}
