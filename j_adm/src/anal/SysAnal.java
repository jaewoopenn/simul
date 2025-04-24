package anal;

import util.MFile;



public class SysAnal {
	protected int g_step;
	protected int g_sys_num;
	protected String g_cfg_fn;
	protected String g_path;
	protected String g_ts_name;
	public boolean isWrite=true;
	protected MFile g_fu;
	
	
	
	
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
}
