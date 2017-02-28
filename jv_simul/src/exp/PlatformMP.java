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
	private int[] g_range;
	public PlatformMP(int ncpu){
		g_ncpu=ncpu;
	}
	public void setRange(int[] r){
		g_range=r;
	}
	
	public void writeCfg(ConfigGen cfg) {
		cfg.setParam("subfix", g_path+"/ts");
		for(int r:g_range){
			cfg.setParam("num",g_sys_num+"");
			writeCfg_i(cfg,r);
			cfg.write(getCfgFN(r));
		}
	}
	
	private void writeCfg_i(ConfigGen cfg, int mod) {
		String modStr=g_ts_name+"_"+(mod);
		cfg.setParam("u_lb", (mod-g_step)*1.0/100+"");
		cfg.setParam("u_ub", (mod)*1.0/100+"");
		cfg.setParam("mod", modStr);
	}
	
	public void genTS(Anal an) {
		boolean bCheck=true;
		if(an==null)
			bCheck=false;
		for(int r:g_range){
			Log.prn(3,"loop "+r);
			ConfigGen cfg=new ConfigGen(getCfgFN(r));
			cfg.readFile();
			SimGen eg=new SimGenMP(cfg,an, g_ncpu);
			eg.setCheck(bCheck);
			eg.gen();
		}
	}
	public void genTS() {
		genTS(null);
	}
	
	public void simul() {
		write_x_axis();
		simul_in(1,new AnalMP(),1);
		simul_in(2,new AnalMP(),2);
	}
	
	public void simul_in(int algo_num,Anal an,int simul_no){
		g_fu=new FUtil();
		for(int r:g_range){
			setRS(r+"");
			if(isWrite)
				g_fu=new FUtil(getRsFN(algo_num));
			for(int j:MUtil.loop(11)){
				setProb(j*0.1);
				simul_in_i(r,an,simul_no);
			}
			g_fu.save();
		}
		
	}
	
	public void simul_in_i(int r,Anal an,int simul_no)
	{
		SysMng sm=new SysMng();
		sm.setProb(g_prob);
		double sum=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(r));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		eg.initCores(g_ncpu);
		int size=eg.size();
		for(int j:MUtil.loop(size)){
			Log.prn(2,"- r:"+r+" fn:"+cfg.get_fn(j));
			TaskMng tm=TaskMng.getFile(cfg.get_fn(j));
			Partition p=new Partition(an,tm.getTaskSet());
			p.anal();
//			p.check();
			CoreMng cm=p.getCoreMng(g_ncpu);
			checkPart(cm);
			eg.loadCM(cm,an,simul_no,sm);
			eg.check();
//			eg.prnTasks();
			eg.simul(0,g_dur);
			for(int k:MUtil.loop(g_ncpu)){
				SimulInfo si=eg.getSI(k);
				sum+=si.getDMR();
//				sum+=si.mig;
			}
			Log.prn(2, sum);
		}
		double avg=sum/size/g_ncpu;
		Log.prn(3, r+","+MUtil.getStr(g_prob)+":"+MUtil.getStr(avg)+",");
		g_fu.print(MUtil.getStr(avg));
		
	}

	public void simul_one(Anal an,
			int simul_no, int r, int j) {
		ConfigGen cfg=new ConfigGen(getCfgFN(r));
		cfg.readFile();
		ExpSimulMP eg=new ExpSimulMP(cfg);
		String fn=cfg.get_fn(j);
		Log.prn(2,"i:"+r+" fn:"+fn);
		TaskMng tm=TaskMng.getFile(fn);
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		CoreMng cm=p.getCoreMng(g_ncpu);
		checkPart(cm);
		SysMng sm=new SysMng();
		sm.setProb(0.5);
		eg.loadCM(cm,an,simul_no,sm);
		eg.simul(0,g_dur);
		SimulInfo si=eg.getSI(0);
		Log.prn(3, r+","+j+","+si.getDMR()+","+si.ms);
	}

	
	private void checkPart(CoreMng cm) {
		int cpus=cm.size();
		if(cpus!=g_ncpu){
			Log.prn(9, "ERROR: ncpu:"+g_ncpu+", cm cpu"+cpus);
			System.exit(1);
		}
	}
	
	public void write_x_axis() {

		for(int r:g_range){
			g_RS=r+"";
			FUtil fu=new FUtil(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_x.txt");
			for(int j:MUtil.loop(11)){
				double x=(j*0.1);
				fu.print(x+"");
			}
			fu.save();
		}		
	}	
	public void write_x_axis2() {
		FUtil fu=new FUtil(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_x.txt");
		for(int j:g_range){
			fu.print((double)j/100+"");
		}
		fu.save();
	}	
	
	public void anal() {
		write_x_axis2();
		anal_in(1,new AnalMP());
		anal_in(2,new AnalEDF_VD());
		anal_in(3,new AnalEDF());
	}
	public void anal_in(int algo_num,Anal an){
		g_fu=new FUtil();
		if(isWrite)
			g_fu=new FUtil(getRsFN(algo_num));
		for(int i:g_range){
			anal_in_i(i,an);			
		}
		g_fu.save();
		
	}
	public void anal_in_i(int r,Anal an){
		int sum=0;
		ConfigGen cfg=new ConfigGen(getCfgFN(r));
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
		Log.prn(3, r+":"+avg);
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
	private String getCfgFN(int mod){
		String modStr=g_ts_name+"_"+mod;
		return g_path+"/"+g_cfg_fn+modStr+".txt";
	}
	private String getRsFN(int no){
		return g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+no+".txt";
	}
	
}
