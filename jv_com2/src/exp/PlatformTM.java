package exp;


import gen.ConfigGen;
import gen.SimGen;
import gen.SimGenTM;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import sim.mc.TaskSimulMC;
import sim.mc.TaskSimul_EDF_AD_E;
import sim.mc.TaskSimul_EDF_VD;
import anal.Anal;
import anal.AnalEDF;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import task.TaskMng;
import task.TaskSetUtil;
import util.Log;
import util.MList;
import util.MUtil;

public class PlatformTM extends Platform{
	
	// task
	public void writeCfg(ConfigGen cfg) {
		cfg.setParam("subfix", g_path+"/ts");
		for(int i=0;i<g_size;i++){
			writeCfg_i(cfg,i);
		}
	}
	
	private void writeCfg_i(ConfigGen cfg, int i) {
		int mod=i*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		cfg.setParam("num",g_sys_num+"");
		if(g_kinds==0){
			cfg.setParam("u_lb", (mod-g_step)*1.0/100+"");
			cfg.setParam("u_ub", (mod)*1.0/100+"");
		} else{
			cfg.setParam("u_lb", "0.75");
			cfg.setParam("u_ub", "0.80");
			cfg.setParam("prob_hi",(mod*1.0/100)+"");
		}
		cfg.setParam("mod", modStr);
		cfg.write(getCfgFN(i));
	}

	public void genTS(boolean b) {
		for(int i:MUtil.loop(g_size)){
			ConfigGen cfg=new ConfigGen(getCfgFN(i));
			cfg.readFile();
			SimGen eg=new SimGenTM(cfg,new AnalEDF_VD());
			eg.setCheck(b);
			eg.gen();
		}
		Log.prn(3, "task");
		
	}
	public void simul() {
		write_x_axis();
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
		simul_in(2,new AnalEDF_AD_E(),new TaskSimul_EDF_AD_E());
	}
	public void simul_in(int no,Anal an,TaskSimulMC tsim){
		g_fu=new MList();
		
		tsim.isSchTab=false;
		for(int i:MUtil.loop(g_size)){
			simul_in_i(i,an,tsim);
		}
		if(isWrite)
			g_fu.save(getRsFN(no));
		
	}
	
	public void simul_in_i(int i,Anal an,TaskSimulMC tsim)
	{
		double sum=0;
		int sum_ms=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		int size=eg.size();
		for(int j:MUtil.loop(size)){
			TaskMng tm=(TaskSetUtil.loadFile(new MList(cfg.get_fn(j)))).getTM();
//			TaskMng tm=TaskMng.getFile(cfg.get_fn(j));
			SysMng sm=new SysMng();
			sm.setMS_Prob(g_prob);
			an.init(tm);
			an.prepare();
			sm.setX(an.computeX());
			tsim.init_sm_tm(sm,tm);
			eg.initSim(0, tsim);
			eg.simul(0,g_dur);
			SimulInfo si=eg.getSI(0);
			double ret=si.getDMR();
			Log.prnc(2, j+","+ret+","+si.ms);
			sum+=ret;
			sum_ms+=si.ms;
			Log.prn(2, " "+sum);
		}
		double avg=sum/size;
		double avg_ms=(sum_ms*1.0/size);
		Log.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg)+","+avg_ms);
		g_fu.add(avg+"");
		
	}
	
	public void simul_vd() {
		isWrite=false;
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
	}

	public void simul_one(int anal, int set, int no) {
		if(anal==0)
			simul_one(new AnalEDF_VD(),new TaskSimul_EDF_VD(),set,no);
		else
			simul_one(new AnalEDF_AD_E(),new TaskSimul_EDF_AD_E(),set,no);
	}
	
	public void simul_one(Anal an,
			TaskSimulMC tsim, int i, int j) {
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		String fn=cfg.get_fn(j);
		TaskMng tm=(TaskSetUtil.loadFile(new MList(fn))).getTM();
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		an.init(tm);
		an.prepare();
		tm.setX(an.computeX());
		tsim.init_sm_tm(sm,tm);
		eg.initSim(0, tsim);
		eg.simul(0,g_dur);
		SimulInfo si=eg.getSI(0);
		Log.prn(2, i+","+j+","+si.getDMR()+","+si.ms);
	}

	
	
	public void anal() {
		write_x_axis();
		anal_in(1,new AnalEDF_AD_E());
		anal_in(2,new AnalEDF_VD());
//		anal_in(3,new AnalEDF_AD());
//		anal_in(4,new AnalICG());
		anal_in(5,new AnalEDF());
	}
	
	public void anal_in(int algo_num,Anal an){
		g_fu=new MList();
		for(int i=0;i<g_size;i++){
			anal_in_i(i,an);			
		}
		if(isWrite)
			g_fu.save(getRsFN(algo_num));
		
	}
	public void anal_in_i(int i,Anal an){
		int sum=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		int size=eg.size();
		for(int j=0;j<size;j++){
			String fn=cfg.get_fn(j);
			TaskMng tm=(TaskSetUtil.loadFile(new MList(fn))).getTM();
			int ret=eg.anal(tm,an);
			Log.prn(2, j+","+ret);
			sum+=ret;
//			Log.prn(2, " "+sum);
		}
		double avg=(double)sum/size;
		Log.prn(3, (g_start+i*g_step)+":"+avg);
		g_fu.add(avg+"");
	}
	
	public void anal_one(int kinds, int set, int no) {
		if(kinds==0)
			anal_one(new AnalEDF_VD(),set,no);
		else
			anal_one(new AnalEDF_AD_E(),set,no);
	}

	public void anal_one(Anal an, int i, int j) {
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulTM eg=new ExpSimulTM(cfg);
		String fn=cfg.get_fn(j);
		TaskMng tm=(TaskSetUtil.loadFile(new MList(fn))).getTM();
		int ret=eg.anal(tm,an);
		Log.prn(3, i+","+j+":"+ret);
		
	}	
	
	
	public void prnTasks() {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			ExpSimulTM eg=new ExpSimulTM(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				String fn=cfg.get_fn(j);
				TaskMng tm=(TaskSetUtil.loadFile(new MList(fn))).getTM();
				Log.prn(2, mod+" "+j);
				tm.getInfo().prn();
			}
		}
	}

	// get 
	private String getCfgFN(int i){
		String modStr=g_ts_name+"_"+(i*g_step+g_start);
		return g_path+"/"+g_cfg_fn+modStr+".txt";
		
	}

	private String getRsFN(int no){
		return g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+no+".txt";
	}
	
}
