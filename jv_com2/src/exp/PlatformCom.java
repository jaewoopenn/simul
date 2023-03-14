package exp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.SysMng;
import sim.com.TaskSimulCom;
import sim.com.TaskSimulCom_FC;
import sim.com.TaskSimulCom_NA;
import sim.com.TaskSimulCom_NA2;
import task.TaskMng;
import anal.Anal;
import anal.AnalEDF_VD;
import comp.AnalComp;
import comp.CompFile;
import comp.CompMng;
import comp.SimCompGen;
import util.SLog;
import util.MList;
import util.MUtil;

public class PlatformCom extends Platform{
	// com
	protected double g_alpha_l=-1;
	protected double g_alpha_u=-1;
	public void setAlpha(double l,double u) {
		this.g_alpha_l=l;
		this.g_alpha_u=u;
		SLog.prn(3, l+","+u);
	}
	
	public void writeComCfg(ConfigGen cfg) {
		cfg.setParam("subfix", g_path+"/ts");
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			cfg.setParam("num",g_sys_num+"");
			if(g_kinds==0){
				cfg.setParam("u_lb", (mod-g_step)*1.0/100+"");
				cfg.setParam("u_ub", (mod)*1.0/100+"");
			} else{
				cfg.setParam("prob_hi",(mod*1.0/100)+"");
			}
			cfg.setParam("mod", modStr);
			cfg.setParam("a_lb", g_alpha_l+"");
			cfg.setParam("a_ub", g_alpha_u+"");
			cfg.write(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		}
	}
	public void genCom(boolean bCheck) {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			SLog.prn(3, modStr);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			SimCompGen eg=new SimCompGen(cfg);
			eg.gen(bCheck);
		}
		SLog.prn(3, "com");
		
	}
	
	public void simulCom(int kinds) { // 0 FC, 1 NA
		TaskSimulCom tsim;
		if(kinds==0)
			tsim=new TaskSimulCom_FC();
		else if(kinds==1)
			tsim=new TaskSimulCom_NA();
		else
			tsim=new TaskSimulCom_NA2();
		MList fu=new MList();
		SLog.prn(3, "prob:"+g_prob);
		for(int i=0;i<g_size;i++){
			double sum=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			int size=cfg.getSize();
			int tot=size;
			for(int j=0;j<size;j++){
				CompMng cm=loadCM(cfg.get_fn(j), kinds);
				SimulInfo si=simul_com_in(cm,tsim);
				double dmr=si.getDMR();
				sum+=dmr;
//				if(dmr==0)
//					tot--;
				SLog.prn(2, " "+sum);
			}
			double avg=0;
			if(tot!=0)
				avg=sum/tot;
			SLog.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg));
			if(isWrite)
				fu.add(avg+"");
		}
		if(isWrite)
			fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+kinds+".txt");
		
	}
	private SimulInfo simul_com_in(CompMng cm, TaskSimulCom tsim) {
		TaskMng tm=cm.getTM();
//		if(AnalEDF_VD.dtm(tm)==0) {
//			return new SimulInfo();
//		}

		
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		double x=AnalEDF_VD.computeX(tm);
		if(x==0) {
			return new SimulInfo();
		}
		sm.setX(x);
		tsim.init_sm_tm(sm,tm);
		tsim.set_cm(cm);
		tsim.simul(0,g_dur);
		tsim.simul_end();
		SimulInfo si=tsim.getSI();
//		SLog.prnc(2, cm+","+si.getDMR()+","+si.ms);
		si.prn();		
		return si;
	}
	public void simulCom_one(int kinds, int set, int no) {
		TaskSimulCom tsim;
		if(kinds==0)
			tsim=new TaskSimulCom_FC();
		else if(kinds==1)
			tsim=new TaskSimulCom_NA();
		else
			tsim=new TaskSimulCom_NA2();
		
//		SLog.prn(1, "no:"+kinds);
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		
		CompMng cm=loadCM(cfg.get_fn(no),kinds);
		SLog.prnc(3, modStr+":"+set+","+no+":");
		simul_com_one_in(tsim,cm);
	}
	
	private void simul_com_one_in(TaskSimulCom tsim, CompMng cm) {
		
		SimulInfo si=simul_com_in(cm,tsim);
		SLog.prn(3, si.getDMR()+","+tsim.getName());
	}
	
	public int analCom(int set, int no,int kinds) {
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		int ret=analCom_in(cfg.get_fn(no), kinds);
		SLog.prn(2, set+","+no+":"+ret);
		return ret;
	}
	
	public void analCom(int kinds) {
		int ret;
		MList fu=new MList();

		for(int i=0;i<g_size;i++){
			int sum=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			int size=cfg.getSize();
			for(int j=0;j<size;j++){
				ret=analCom_in(cfg.get_fn(j), kinds);
				SLog.prn(2, j+","+ret);
				sum+=ret;
//				Log.prn(2, " "+sum);
			}
			double avg=(double)sum/size;
			SLog.prn(3, (g_start+i*g_step)+":"+avg);
			if(isWrite)
				fu.add(avg+"");
		}
		if(isWrite)
			fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+".txt");
		
		
	}
	private int analCom_in(String fn, int kinds) {
		CompMng cm=loadCM_a(fn);
		return analComp(cm,kinds);
	}
	public void prnCom() {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			int size=cfg.getSize();
			for(int j=0;j<size;j++){
				CompMng cm=loadCM(cfg.get_fn(j),0);
				SLog.prn(3, mod+" "+j+" "+cm.getMCUtil());
			}
		}
		
	}

	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
//		Log.prn(2, ""+a.getDtm());
		boolean b=a.is_sch();
		return MUtil.btoi(b);
	}
	
	//comp
	public CompMng loadCM(String fn,int kinds){
		SLog.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
		cm.part();
		double x=AnalEDF_VD.computeX(cm.getTM());
		if(kinds==2)
			cm.setX2(x);
		else
			cm.setX(x);
		
//		double u=cm.analMaxRes();
////		SLog.prn(3, "MCUtil:"+cm.getMCUtil()+", MaxUtil:"+cm.getMaxUtil()+", WC_Util:"+u);
//		if(u>1+MUtil.err) {
//			SLog.err("com: not schedulable:"+u);
//		}
		return cm;
	}
	public CompMng loadCM_a(String fn){
		SLog.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
		if(g_alpha_l==-1||g_alpha_u==-1) {
			SLog.err("alpha error:"+g_alpha_l+","+g_alpha_u);
		}
		cm.setAlpha(g_alpha_l,g_alpha_u);
		cm.part();
//		double x=AnalEDF_VD.computeX(cm.getTM());
		
		return cm;
	}
	
	public int analComp(CompMng cm,int kinds) {
		AnalComp a=new AnalComp(cm);
		if(a.computeX(kinds)==false) {
			return 0;
		}
		cm.analMaxRes();
		return 	a.anal(kinds);

	}	
	
}
