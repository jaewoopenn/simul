package exp;


import gen.ConfigGen;
import gen.SimGen;
import gen.SimGenMP;
import part.CoreMng;
import part.Partition;
import anal.Anal;
import anal.AnalEDF;
import anal.AnalEDF_VD;
import anal.AnalMP;
import basic.TaskMng;
import simul.SimulInfo;
import util.FUtil;
import util.Log;
import util.MUtil;

public class PlatformMP extends Platform{
	private int g_ncpu;
	public PlatformMP(int ncpu){
		g_ncpu=ncpu;
	}
	
	public void writeCfg(ConfigGen cfg) {
		cfg.setParam("subfix", g_path+"/ts");
		for(int i=0;i<g_size;i++){
			cfg.setParam("num",g_sys_num+"");
			writeCfg_i(cfg,i);
			cfg.write(getCfgFN(i));
		}
	}
	
	private void writeCfg_i(ConfigGen cfg, int i) {
		int mod=i*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		if(g_kinds==0){
			cfg.setParam("u_lb", (mod-g_step)*1.0/100+"");
			cfg.setParam("u_ub", (mod)*1.0/100+"");
		} else{
			cfg.setParam("u_lb", "0.75");
			cfg.setParam("u_ub", "0.80");
			cfg.setParam("prob_hi",(mod*1.0/100)+"");
		}
		cfg.setParam("mod", modStr);
	}
	
	public void genTS(boolean b) {
		for(int i:MUtil.loop(g_size)){
			ConfigGen cfg=new ConfigGen(getCfgFN(i));
			cfg.readFile();
			SimGen eg=new SimGenMP(cfg,new AnalEDF_VD(), g_ncpu);
			eg.setCheck(b);
			eg.gen();
		}
		Log.prn(3, "task");
	}
	
	public void simul() {
		write_x_axis();
		simul_in(1,new AnalMP(),1);
		simul_in(2,new AnalMP(),2);
	}
	
	public void simul_in(int algo_num,Anal an,int simul_no){
		g_fu=new FUtil();
		if(isWrite)
			g_fu=new FUtil(getRsFN(algo_num));
		for(int i:MUtil.loop(g_size)){
			simul_in_i(i,an,simul_no);
		}
		g_fu.save();
		
	}
	
	public void simul_in_i(int i,Anal an,int simul_no)
	{
		double sum=0;
		int sum_ms=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		eg.initCores(g_ncpu);
		int size=eg.size();
		for(int j:MUtil.loop(size)){
			TaskMng tm=TaskMng.getFile(cfg.get_fn(j));
			Log.prn(2,"i:"+i+" fn:"+cfg.get_fn(j));
			Partition p=new Partition(an,tm.getTaskSet());
			p.anal();
			CoreMng cm=p.getCoreMng();
			checkPart(cm);
			eg.loadCM(cm,an,simul_no);
			eg.simul(0,g_dur);
			SimulInfo si=eg.getSI(0);
			double dmr=si.getDMR();
			Log.prnc(2, j+","+dmr+","+si.ms);
			sum+=dmr;
			sum_ms+=si.ms;
			Log.prn(2, " "+sum);
		}
		double avg=sum/size;
		double avg_ms=(sum_ms*1.0/size);
		Log.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg)+","+avg_ms);
		g_fu.print(avg+"");
		
	}

	public void simul_one(Anal an,
			int simul_no, int i, int j) {
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		String fn=cfg.get_fn(j);
		Log.prn(2,"i:"+i+" fn:"+fn);
		TaskMng tm=TaskMng.getFile(fn);
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		CoreMng cm=p.getCoreMng();
		checkPart(cm);
		eg.loadCM(cm,an,simul_no);
		eg.simul(0,g_dur);
		SimulInfo si=eg.getSI(0);
		Log.prn(3, i+","+j+","+si.getDMR()+","+si.ms);
	}

	
	private void checkPart(CoreMng cm) {
		int cpus=cm.size();
		if(cpus!=g_ncpu){
			Log.prn(9, "ERROR: ncpu:"+g_ncpu+", cm cpu"+cpus);
			System.exit(1);
		}
	}
	
	public void anal() {
		write_x_axis();
		anal_in(1,new AnalEDF());
		anal_in(2,new AnalEDF_VD());
	}
	public void anal_in(int algo_num,Anal an){
		g_fu=new FUtil();
		if(isWrite)
			g_fu=new FUtil(getRsFN(algo_num));
		for(int i=0;i<g_size;i++){
			anal_in_i(i,an);			
		}
		g_fu.save();
		
	}
	public void anal_in_i(int i,Anal an){
		int sum=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		eg.initCores(g_ncpu);
		int size=eg.size();
		for(int j=0;j<size;j++){
			int ret=eg.anal(TaskMng.getFile(cfg.get_fn(j)),an);
			Log.prn(2, j+","+ret);
			sum+=ret;
//			Log.prn(2, " "+sum);
		}
		double avg=(double)sum/size;
		Log.prn(3, (g_start+i*g_step)+":"+avg);
		g_fu.print(avg+"");
	}

	public void anal_one(Anal an, int i, int j) {
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		eg.initCores(g_ncpu);
		String fn=cfg.get_fn(j);
		TaskMng tm=TaskMng.getFile(fn);
		int ret=eg.anal(tm,an);
		Log.prn(3, i+","+j+":"+ret);
		
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
