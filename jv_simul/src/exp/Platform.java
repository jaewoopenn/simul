package exp;


import basic.TaskMng;
import simul.Anal;
import simul.AnalEDF_AT_S;
import simul.AnalEDF_VD;
import simul.ConfigGen;
import simul.SimGen;
import utilSim.FUtil;
import utilSim.Log;

public class Platform {
	private int g_startUtil;
	private int g_size;
	private String g_cfg_fn;
	private int g_dur;
	private double g_prob;
	private String g_path;
	public boolean isWrite=true;
	public void writeCfg(ConfigGen g_cfg) {
		g_cfg.setParam("subfix", g_path+"/ts");
		int step=5;
		for(int i=0;i<g_size;i++){
			g_cfg.setParam("u_lb", (i*step+g_startUtil)*1.0/100+"");
			g_cfg.setParam("u_ub", (i*step+g_startUtil+5)*1.0/100+"");
			g_cfg.setParam("mod", (i*step+g_startUtil+5)+"");
			g_cfg.write(g_path+"/"+g_cfg_fn+"_"+i+".txt");
		}
	}
	public void genTS() {
		for(int i=0;i<g_size;i++){
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+i+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			eg.gen2();
		}
		Log.prn(3, "task");
		
	}
	public void simul_in(int no,Anal an,TaskSimul ts){
		double ret;
		FUtil fu=null;
		if(isWrite)
			fu=new FUtil(g_path+"/rs/sim"+no+".txt");
		ts.isSchTab=false;
		for(int i=0;i<g_size;i++){
			double sum=0;
			ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+i+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul(cfg);
			eg.setDuration(g_dur);
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
				ts.setTm(tm);
				ret=eg.simul(ts);
				Log.prn(2, j+","+ret);
				sum+=ret;
			}
			double avg=(sum*1.0/size);
			Log.prn(3, (g_startUtil+5+i*5)+":"+sum+"/"+size+","+avg);
			if(isWrite)
				fu.print(avg+"");
		}
		if(isWrite)
			fu.save();
		
	}
	public void simul() {
//		write_x_axis();
		simul_in(1,new AnalEDF_VD(),new TaskSimul_EDF_VD());
//		simul_in(2,new AnalEDF_AT_S(),new TaskSimul_EDF_AT_S());
	}

	public void simul_one(int anal, int set, int no) {
		if(anal==0)
			simul_in_one(new AnalEDF_VD(),new TaskSimul_EDF_VD(),set,no);
		else
			simul_in_one(new AnalEDF_AT_S(),new TaskSimul_EDF_AT_S(),set,no);
	}
	
	private void simul_in_one(Anal an,
			TaskSimul ts, int set, int no) {
		double ret;

		ConfigGen cfg=new ConfigGen(g_path+"/"+g_cfg_fn+"_"+set+".txt");
		cfg.readFile();
		ExpSimul eg=new ExpSimul(cfg);
		eg.setDuration(g_dur);
		TaskMng tm=eg.loadTM(no);
		tm.getInfo().setProb_ms(g_prob); 
		an.init(tm);
		an.prepare();
		tm.setX(an.getX());
		ts.setTm(tm);
		ret=eg.simul(ts);
		Log.prn(2, set+","+no+","+ret);
		
	}
	private void write_x_axis() {
		FUtil fu=new FUtil(g_path+"/rs/x.txt");

		for(int i=0;i<g_size;i++){
			fu.print((double)(g_startUtil+5+i*5)/100+"");
		}		
		fu.save();
	}
	public void setStartUtil(int g_startUtil) {
		this.g_startUtil = g_startUtil;
	}
	public void setSize(int s) {
		this.g_size = s;
	}
	public void setCfg_fn(String t) {
		this.g_cfg_fn = t;
	}
	public void setPath(String t) {
		this.g_path = t;
	}
	public void setDuration(int d ) {
		this.g_dur = d;
	}
	public void setProb(double p){
		this.g_prob=p;
	}
}
