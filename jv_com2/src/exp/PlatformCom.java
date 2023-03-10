package exp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.SysMng;
import sim.com.TaskSimulCom;
import sim.com.TaskSimulCom_FC;
import sim.com.TaskSimulCom_NA;
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
	protected double g_alpha_l;
	protected double g_alpha_u;
	public void setAlpha(double l,double u) {
		this.g_alpha_l=l;
		this.g_alpha_u=u;
		
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
		else
			tsim=new TaskSimulCom_NA();
		MList fu=new MList();
		SLog.prn(3, "prob:"+g_prob);
		for(int i=0;i<g_size;i++){
			double sum=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			int size=cfg.getSize();
			for(int j=0;j<size;j++){
				CompMng cm=loadCM(cfg.get_fn(j));
				SimulInfo si=simul_com_in(cm,tsim);
				sum+=si.getDMR();
				SLog.prn(2, " "+sum);
			}
			double avg=sum/size;
			SLog.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg));
			if(isWrite)
				fu.add(avg+"");
		}
		if(isWrite)
			fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+kinds+".txt");
		
	}
	private SimulInfo simul_com_in(CompMng cm, TaskSimulCom tsim) {
		Anal an=new AnalEDF_VD();
		cm.setAlpha(g_alpha_l,g_alpha_u);
		TaskMng tm=cm.getTM();
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		an.init(tm);

		sm.setX(an.computeX());
		if(an.getDtm()==0) {
			return new SimulInfo();
		}
		tsim.init_sm_tm(sm,tm);
		tsim.set_cm(cm);
		tsim.simul(0,g_dur);
		tsim.simul_end();
		SimulInfo si=tsim.getSI();
//		SLog.prnc(2, cm+","+si.getDMR()+","+si.ms);
		return si;
	}
	public void simulCom_one(int kinds, int set, int no) {
		if(kinds==0)
			simul_com_one_in(new TaskSimulCom_FC(),set,no);
		else
			simul_com_one_in(new TaskSimulCom_NA(),set,no);
	}
	
	private void simul_com_one_in(TaskSimulCom tsim, int set, int no) {
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		
		//		si.prn();
		CompMng cm=loadCM(cfg.get_fn(no));
		SimulInfo si=simul_com_in(cm,tsim);
		SLog.prn(3, modStr+":"+set+","+no+":"+si.getDMR()+","+tsim.getName());
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
		CompMng cm=loadCM(fn);
		cm.setAlpha(g_alpha_l,g_alpha_u);
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
				CompMng cm=loadCM(cfg.get_fn(j));
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
	public CompMng loadCM(String fn){
		SLog.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
		return cm;
	}
	
	public int analComp(CompMng cm,int kinds) {
		cm.part();
		cm.analMaxRes();
		AnalComp a=new AnalComp(cm);
		a.computeX();
		return 	a.anal(kinds);

	}	
	
}
