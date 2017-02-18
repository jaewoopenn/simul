package exp;


import gen.ConfigGen;
import gen.SimGen;
import anal.Anal;
import anal.AnalEDF;
import anal.AnalEDF_AD;
import anal.AnalEDF_AD_E;
import anal.AnalEDF_VD;
import anal.AnalICG;
import basic.TaskMng;
import simul.SimulInfo;
import simul.TaskSimul;
import simul.TaskSimul_EDF_AD_E;
import simul.TaskSimul_EDF_VD;
import util.FUtil;
import util.Log;
import util.MUtil;

public class PlatformMP extends Platform{
	
	// task
	public void writeCfg(ConfigGen cfg) {
		cfg.setParam("subfix", g_path+"/ts");
		for(int i=0;i<g_size;i++){
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
			cfg.write(g_path+"/"+g_cfg_fn+modStr+".txt");
		}
	}
	
	public void genTS(boolean bCheck) {
		for(int i:MUtil.loop(g_size)){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			Log.prn(3, modStr);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+modStr+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			if(bCheck)
				eg.gen2();
			else
				eg.gen();
		}
		Log.prn(3, "task");
		
	}
	public void simul() {
		write_x_axis();
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
		simul_in(2,new AnalEDF_AD_E(),new TaskSimul_EDF_AD_E());
	}
	public void simul_in(int no,Anal an,TaskSimul ts){
		double ret;
		FUtil fu=null;
		if(isWrite)
			fu=new FUtil(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+no+".txt");
		ts.isSchTab=false;
		for(int i=0;i<g_size;i++){
			double sum=0;
			int sum_ms=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+modStr+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul();
			eg.setCfg(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				TaskMng tm=eg.loadTM(j);
				tm.getInfo().setProb_ms(g_prob); 
				an.init(tm);
				an.prepare();
				tm.setX(an.getX());
//				ret=eg.anal(tm,new AnalEDF_AT_S());
//				if(ret==0){
//					continue;
//				}
				ts.set_tm(tm);
				SimulInfo si=eg.simul(ts,g_dur);
				ret=si.getDMR();
				Log.prnc(2, j+","+ret+","+si.ms);
				sum+=ret;
				sum_ms+=si.ms;
				Log.prn(2, " "+sum);
			}
			double avg=sum/size;
			double avg_ms=(sum_ms*1.0/size);
			Log.prn(3, (g_start+i*g_step)+":"+MUtil.getStr(avg)+","+avg_ms);
			if(isWrite)
				fu.print(avg+"");
		}
		if(isWrite)
			fu.save();
		
	}
	public void simul_vd() {
		isWrite=false;
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
	}

	public void simul_one(int anal, int set, int no) {
		if(anal==0)
			simul_in_one(new AnalEDF_VD(),new TaskSimul_EDF_VD(),set,no);
		else
			simul_in_one(new AnalEDF_AD_E(),new TaskSimul_EDF_AD_E(),set,no);
	}
	
	private void simul_in_one(Anal an,
			TaskSimul ts, int set, int no) {
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		ExpSimul eg=new ExpSimul();
		eg.setCfg(cfg);
		TaskMng tm=eg.loadTM(no);
		tm.getInfo().setProb_ms(g_prob); 
		an.init(tm);
		an.prepare();
		tm.setX(an.getX());
		ts.set_tm(tm);
		SimulInfo si=eg.simul(ts,g_dur);
		Log.prn(2, set+","+no+","+si.getDMR()+","+si.ms);
		
	}

	
	
	public void anal() {
		write_x_axis();
		anal_in(1,new AnalEDF_AD_E());
		anal_in(2,new AnalEDF_VD());
		anal_in(3,new AnalEDF_AD());
		anal_in(4,new AnalICG());
		anal_in(5,new AnalEDF());
	}
	
	public void anal_in(int kinds,Anal an){
		int ret;
		FUtil fu=null;
		Log.prn(3, "anal:"+an.getName());
		if(isWrite)
			fu=new FUtil(g_path+"/rs/"+g_ts_name+"_"+g_RS+"_"+kinds+".txt");
		for(int i=0;i<g_size;i++){
			int sum=0;
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul();
			eg.setCfg(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				TaskMng tm=eg.loadTM(j);
				ret=eg.anal(tm,an);
				Log.prn(2, j+","+ret);
				sum+=ret;
//				Log.prn(2, " "+sum);
			}
			double avg=(double)sum/size;
			Log.prn(3, (g_start+i*g_step)+":"+avg);
			if(isWrite)
				fu.print(avg+"");
		}
		if(isWrite)
			fu.save();
		
	}
	
	public void anal_one(int kinds, int set, int no) {
		if(kinds==0)
			anal_in_one(new AnalEDF_VD(),set,no);
		else
			anal_in_one(new AnalEDF_AD_E(),set,no);
	}
	private void anal_in_one(Anal an,
			int set, int no) {
		int ret;
		int mod=set*g_step+g_start;
		String modStr=g_ts_name+"_"+(mod);
		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
		cfg.readFile();
		ExpSimul eg=new ExpSimul();
		eg.setCfg(cfg);
		TaskMng tm=eg.loadTM(no);
		tm.prn();
		ret=eg.anal(tm,an);
		Log.prn(2, set+","+no+","+ret);
		
	}
	
	
	public void prnTasks() {
		for(int i=0;i<g_size;i++){
			int mod=i*g_step+g_start;
			String modStr=g_ts_name+"_"+(mod);
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+modStr+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul();
			eg.setCfg(cfg);
			int size=eg.size();
			for(int j=0;j<size;j++){
				TaskMng tm=eg.loadTM(j);
				Log.prn(2, mod+" "+j);
				tm.getInfo().prn();
			}
		}
		
	}
}
