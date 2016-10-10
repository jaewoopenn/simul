package basic;

import utilSim.Log;

public class Task {
	public int tid;
	public int period;
	public int c_l;
	public int c_h;
	public double vd;
	public boolean is_HI=false;
	public boolean is_HM=false;
	public boolean is_dropped=false;

	private int cid=-1;
	private boolean is_isolated=false;

	public Task(int tid,int period, int c_l) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_l;
	}

	public Task(int tid,int period, int c_l, int c_h) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HI=true;
	}
	public void ms(){
		this.is_HM=true;
	}
	public void drop() {
		this.is_dropped=true;
	}
	public void setVD(double vd){
//		System.out.println("tid:"+tid+" vd:"+vd);
		this.vd=vd;
	}
	public void setComp(int id) {
		cid=id;
	}
	public void setIsolate(boolean b) {
		is_isolated=b;
	}
	
	public void prn() {
		Log.prnc(2, "tid:"+tid);
		Log.prnc(2, " p:"+period);
		if (is_HI){
			Log.prnc(2," cl:"+c_l+" ch:"+c_h+" vd:"+vd);
			Log.prnc(2," isHM:"+is_HM);
			
		}else{
			Log.prnc(2," cl:"+c_l);
			Log.prnc(2," isDrop:"+is_dropped);
		}
		Log.prnc(2," hi-crit?"+is_HI);
		Log.prn(2," util:"+getLoUtil());
	}
	public void prnComp(){
		Log.prnc(2, "cid:"+cid);
		Log.prnc(2, " isol:"+is_isolated+" ");
		prn();
	}
	public void prnStat() {
		Log.prnc(2, "tid:"+tid);
		if (is_HI){
			Log.prn(2," isHM:"+is_HM);
			
		}else{
			Log.prn(2," isDrop:"+is_dropped);
		}
	}
	public boolean check() {
		if (period==0)
			return false;
		if (c_h==0)
			return false;
		if (c_l==0)
			return false;
		return true;
	}
	public double getLoUtil(){
		return (double)c_l/period;
	}
	public double getHiUtil(){
		return (double)c_h/period;
	}
	public double getLoRUtil(){
		return (double)c_l/vd;
	}

	public int getComp(){
		return cid;
	}
	public boolean is_isol(){
		return is_isolated;
	}




}

