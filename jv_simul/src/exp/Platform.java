package exp;


import simul.ConfigGen;
import simul.SimGen;
import utilSim.Log;

public class Platform {
	private int g_startUtil;
	private int g_size;
	private String g_cfg_fn;
	private int g_dur;
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
			eg.gen();
		}
		Log.prn(3, "task");
		
	}
	public void simul() {
		for(int i=0;i<g_size;i++){
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul(cfg);
			eg.setDuration(g_dur);
			int size=eg.size();
			int ret=eg.simul();
			Log.prn(3, (g_startUtil+5+i*5)+":"+ret+"/"+size+","+(ret*1.0/size));
		}
	}
	public void anal() {
		for(int i=0;i<g_size;i++){
			ConfigGen cfg=new ConfigGen("exp/cfg/cfg_"+i+".txt");
			cfg.readFile();
			ExpSimul eg=new ExpSimul(cfg);
			int size=eg.size();
			int ret=eg.anal();
			Log.prn(3, (g_startUtil+5+i*5)+":"+ret+"/"+size+","+(ret*1.0/size));
		}
		
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

}
