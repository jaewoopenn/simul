package exp;

import anal.Anal;
import task.TaskMng;
import util.MList;



public abstract class Platform {
	protected int g_kinds;
	protected int g_start;
	protected int g_size;
	protected int g_step;
	protected int g_sys_num;
	protected String g_cfg_fn;
	protected int g_dur;
	protected double g_prob;
	protected String g_path;
	protected String g_ts_name;
	public boolean isWrite=true;
	protected String g_RS;
	protected MList g_fu;
	public void write_x_axis() {
		MList fu=new MList();

		for(int i=0;i<g_size;i++){
			fu.add((double)(g_start+i*g_step)/100+"");
		}		
		fu.save(g_path+"/rs/"+g_ts_name+"_"+g_RS+".txt");
	}
	
	public void setKinds(int d) {
		this.g_kinds = d;
	}
	public void setStart(int d) {
		this.g_start = d;
	}
	public void setSize(int d) {
		this.g_size = d;
	}
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
	
	public abstract int anal(TaskMng tm, Anal a) ;

}
