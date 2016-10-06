package exp;


import basic.TaskMng;
import simul.Anal;
import simul.AnalEDF_AT_S;
import simul.ConfigGen;
import simul.SimGen;
import utilSim.Log;

public class Platform {
	private int g_startUtil;
	private int g_size;
	private String g_cfg_fn;
	private int g_dur;
	private double g_prob;
	public void writeCfg(ConfigGen g_cfg) {
		int step=5;
		for(int i=0;i<g_size;i++){
			g_cfg.setParam("u_lb", (i*step+g_startUtil)*1.0/100+"");
			g_cfg.setParam("u_ub", (i*step+g_startUtil+5)*1.0/100+"");
			g_cfg.setParam("mod", (i*step+g_startUtil+5)+"");
			g_cfg.write(g_cfg_fn+"_"+i+".txt");
		}
	}
	public void genTS() {
		for(int i=0;i<g_size;i++){
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
			cfg.readFile();
			SimGen eg=new SimGen(cfg);
			eg.gen2();
		}
		Log.prn(3, "task");
		
	}
	public void simul_in(Anal an,TaskSimul ts){
		double ret;

		for(int i=0;i<g_size;i++){
			double sum=0;
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
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
				ret=eg.simul(tm,ts);
				Log.prn(2, j+","+ret);
				sum+=ret;
			}

			Log.prn(3, (g_startUtil+5+i*5)+":"+sum+"/"+size+","+(sum*1.0/size));
		}
		
	}
	public void simul() {
		simul_in(new AnalEDF_AT_S(),new TaskSimul_EDF_AT_S());
	}

	
	public void setStartUtil(int g_startUtil) {
		this.g_startUtil = g_startUtil;
	}
	public void setSize(int g_size) {
		this.g_size = g_size;
	}
	public void setCfg_fn(String g_cfg_fn) {
		this.g_cfg_fn = g_cfg_fn;
	}
	public void setDuration(int dur ) {
		this.g_dur = dur;
	}
	public void setProb(double p){
		this.g_prob=p;
	}
}
