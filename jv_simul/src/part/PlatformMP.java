package part;


import gen.ConfigGen;
import gen.SimGen;
import anal.Anal;
import anal.AnalEDF_VD;
import basic.TaskMng;
import basic.TaskSetFix;
import exp.Platform;
import simul.SimulInfo;
import simul.TaskSimul;
import simul.TaskSimul_EDF_VD;
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
			SimGen eg=new SimGen(cfg);
			eg.setCheck(b);
			eg.gen();
		}
		Log.prn(3, "task");
	}
	
	public void simul() {
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
	}
	
	public void simul_in(int algo_num,Anal an,TaskSimul ts){
		g_fu=new FUtil();
		if(isWrite)
			g_fu=new FUtil(getRsFN(algo_num));
		ts.isSchTab=false;
		for(int i:MUtil.loop(g_size)){
			simul_in_i(i,an,ts);
		}
		g_fu.save();
		
	}
	
	public void simul_in_i(int i,Anal an,TaskSimul ts)
	{
		double sum=0;
		int sum_ms=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(i));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(g_ncpu,cfg);
		int size=eg.size();
		for(int j:MUtil.loop(size)){
			TaskMng tm=TaskMng.getFile(cfg.get_fn(j));
			Partition p=new Partition(tm.getTaskSet());
			p.anal();
			checkPart(p);
			for(int k:MUtil.loop(g_ncpu)){
				TaskSetFix tsf=new TaskSetFix(p.getTS(k));
				tm=tsf.getTM();
				an.init(tm);
				an.prepare();
				tm.setX(an.getX());
				eg.init(i,new TaskSimul_EDF_VD(tm));
			}
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
	
	private void checkPart(Partition p) {
		int cpus=p.size();
		if(cpus>g_ncpu){
			Log.prn(9, "ERROR: ncpu>"+g_ncpu);
			System.exit(1);
		}
		if(cpus==1){
			Log.prn(9, "ERROR: ncpu=1");
			System.exit(1);
		}
		
	}
	private String getCfgFN(int i){
		String modStr=g_ts_name+"_"+(i*g_step+g_start);
		return g_path+"/"+g_cfg_fn+modStr+".txt";
	}
	private String getRsFN(int no){
		return g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+no+".txt";
	}
	
}
