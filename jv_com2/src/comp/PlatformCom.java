package comp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import sim.mc.TaskSimulMC;
import task.TaskMng;
import exp.ExpSimulTM;
import exp.Platform;
import anal.Anal;
import anal.AnalEDF_VD;
import util.Log;
import util.MList;
import util.MUtil;

public class PlatformCom extends Platform{
	
	public void writeComCfg(ConfigGen g_cfg) {
		g_cfg.setParam("subfix", g_path+"/ts");
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			g_cfg.setParam("num",g_sys_num+"");
			if(g_kinds==0){
				g_cfg.setParam("u_lb", (mod-g_step)*1.0/100+"");
				g_cfg.setParam("u_ub", (mod)*1.0/100+"");
			} else{
				g_cfg.setParam("prob_hi",(mod*1.0/100)+"");
			}
			g_cfg.setParam("mod", modStr);
			g_cfg.setParam("a_lb", g_a_l+"");
			g_cfg.setParam("a_ub", g_a_u+"");
			g_cfg.write(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		}
	}
	public void genCom(boolean bCheck) {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			Log.prn(3, modStr);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			SimCompGen eg=new SimCompGen(cfg);
			if(bCheck)
				eg.gen2();
			else
				eg.gen();
		}
		Log.prn(3, "com");
		
	}
	public void simulCom(){
		write_x_axis();
		simul_com_in(1,new TaskSimul_FC_MCS());
		simul_com_in(2,new TaskSimul_FC_Naive());
		
	}
	
	private void simul_com_in(int kind, TaskSimulMC tsim) {
		double ret;
		MList fu=new MList();
		tsim.isSchTab=false;
		Anal an=new AnalEDF_VD();
		Log.prn(3, "prob:"+g_prob);
		for(int i=0;i<g_size;i++){
			double sum=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			ExpSimulTM eg=new ExpSimulTM(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				CompMng cm=eg.loadCM(j);
				cm.setAlpha(g_a_l,g_a_u);
				TaskMng tm=cm.getTM();
				SysMng sm=new SysMng();
				sm.setMS_Prob(g_prob);
				an.init(tm);
				an.prepare();
				cm.setX(an.computeX());
				tsim.init_sm_tm(sm,tm);
				tsim.set_cm(cm);
				eg.initSim(0, tsim);
				eg.simul(0,g_dur);
				SimulInfo si=eg.getSI(0);
				ret=si.getDMR();
				Log.prnc(2, j+","+ret+","+si.ms);
				sum+=ret;
				Log.prn(2, " "+sum);
			}
			double avg=sum/size;
			Log.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg));
			if(isWrite)
				fu.add(avg+"");
		}
		if(isWrite)
			fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+kind+".txt");
		
	}
	public void simulCom_one(int kinds, int set, int no) {
		if(kinds==0)
			simul_com_in_one(new TaskSimul_FC_MCS(),set,no);
		else
			simul_com_in_one(new TaskSimul_FC_Naive(),set,no);
	}
	
	private void simul_com_in_one(TaskSimulMC ts, int set,
			int no) {
		double ret;
		Anal an=new AnalEDF_VD();
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		CompMng cm=eg.loadCM(no);
		cm.setAlpha(g_a_l,g_a_u);
		TaskMng tm=cm.getTM();
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		an.init(tm);
		an.prepare();
		cm.setX(an.computeX());
		ts.init_sm_tm(sm,tm);
		ts.set_cm(cm);
		ts.isSchTab=false;
		eg.initSim(0, ts);
		eg.simul(0,g_dur);
		SimulInfo si=eg.getSI(0);
//		si.prn();
		ret=si.getDMR();
		Log.prn(2, set+","+no+":"+ret+","+si.rel);
	}
	
	public int analCom(int set, int no,int kinds) {
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		CompMng cm=eg.loadCM(no);
		cm.setAlpha(g_a_l,g_a_u);
		int ret=eg.analComp(cm,kinds);
		Log.prn(2, set+","+no+":"+ret);
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
			ExpSimulTM eg=new ExpSimulTM(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				CompMng cm=eg.loadCM(j);
				cm.setAlpha(g_a_l,g_a_u);
				ret=eg.analComp(cm,kinds);
				Log.prn(2, j+","+ret);
				sum+=ret;
//				Log.prn(2, " "+sum);
			}
			double avg=(double)sum/size;
			Log.prn(3, (g_start+i*g_step)+":"+avg);
			if(isWrite)
				fu.add(avg+"");
		}
		if(isWrite)
			fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+".txt");
		
		
	}
	public void prnCom() {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			ExpSimulTM eg=new ExpSimulTM(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				CompMng cm=eg.loadCM(j);
				Log.prn(3, mod+" "+j+" "+cm.getMCUtil());
			}
		}
		
	}
	
}
