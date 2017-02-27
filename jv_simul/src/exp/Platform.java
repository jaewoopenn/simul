package exp;

import util.FUtil;



public class Platform {
	protected int g_step;
	protected int g_sys_num;
	protected String g_cfg_fn;
	protected int g_dur;
	protected double g_prob;
	protected String g_path;
	protected String g_ts_name;
	public boolean isWrite=true;
	protected String g_RS;
	protected FUtil g_fu;
	
	
	
	
	public void setStep(int d) {
		this.g_step = d;
	}
	public void setSysNum(int s) {
		this.g_sys_num = s;
	}
	public void setCfg_fn(String t) {
		this.g_cfg_fn = t;
	}
	public void setPath(String t) {
		this.g_path = t;
	}
	public void setTSName(String t) {
		this.g_ts_name = t;
	}
	public void setDuration(int d ) {
		this.g_dur = d;
	}
	public void setProb(double p){
		this.g_prob=p;
	}
	public void setRS(String s) {
		this.g_RS=s;
		
	}
}
