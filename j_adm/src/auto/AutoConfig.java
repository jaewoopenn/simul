package auto;

import gen.ConfigGen;
import util.MList;
import util.SLog;

public  class AutoConfig {
	private boolean g_isMC=false; // not IMC
	private String g_path;
	private int g_num=100;
	private double g_p_hc=0.5;
	private double g_ratio=-1;
	private double g_ratio_hi=-1;

	public AutoConfig(String path) {
		g_path=path;
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
	

	
	
	

	



	
	public void setNum(int n) {
		g_num=n;
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

	
	
	
}
