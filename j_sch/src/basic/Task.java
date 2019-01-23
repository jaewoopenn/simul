package basic;
/*

*/


import util.Log;
import util.MUtil;

public class Task {
	public int tid;
	public int period;
	public int c_l;
	public int c_h;
	public double vd;
	public boolean is_HI=false;
	private boolean is_Hi_Mode=false;
	public boolean is_dropped=false;

	private boolean is_hi_preferred=false;
	

	public Task(int period, int c_l) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_l;
	}

	public Task(int period, int c_l, int c_h) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HI=true;
	}
	public void ms(){
		if(!is_hi_preferred)
			is_Hi_Mode=true;
	}
	public boolean isHM() {
		return is_Hi_Mode;
	}
	public void drop() {
//		Log.prn(1, "drop tid:"+tid);
		this.is_dropped=true;
	}
	public void setVD(double vd){
//		System.out.println("tid:"+tid+" vd:"+vd);
		this.vd=vd;
	}
	public void setHI_only() {
		 is_hi_preferred = true;
		 is_Hi_Mode=true;
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
	public double getLoVdUtil(){
		return (double)c_l/vd;
	}


	public Task getCopy() {
		Task t=new Task(period, c_l);
		return t;
	}
	
	public void prn() {
		Log.prnc(2, "tid:"+tid);
		Log.prnc(2, " p:"+period);
		if (is_HI){
			Log.prnc(2," cl:"+c_l+" ch:"+c_h+" vd:"+vd);
			Log.prnc(2," isHM:"+is_Hi_Mode);
			
		}else{
			Log.prnc(2," cl:"+c_l);
			Log.prnc(2," isDrop:"+is_dropped);
		}
		Log.prnc(2," hi-crit?"+is_HI);
		Log.prn(2," util:"+getLoUtil());
	}
	public void prnShort() {
		Log.prnc(2, tid);
		Log.prnc(2, ", "+period);
		Log.prnc(2, ", "+vd);
		Log.prnc(2, ", "+c_l);
		Log.prnc(2, ", "+c_h);
		Log.prnc(2, ", "+MUtil.getStr(getLoUtil()));
		Log.prn(2, ", "+MUtil.getStr(getHiUtil()));
	}
	
	
	
	public void prnRuntime() {
		Log.prnc(2, "tid:"+tid);
		Log.prnc(2, ", "+MUtil.getStr(getLoUtil()));
		Log.prnc(2, ", "+MUtil.getStr(getHiUtil()));
		if (is_HI){
			Log.prnc(2," isHM:"+is_Hi_Mode);
			Log.prn(2," isHI_Only:"+is_hi_preferred);
			
		}else{
			Log.prn(2," isDrop:"+is_dropped);
		}
		
	}
	public void prnStat() {
		Log.prnc(2, "tid:"+tid);
		Log.prnc(2, ", "+MUtil.getStr(getLoUtil()));
		Log.prnc(2, ", "+MUtil.getStr(getHiUtil()));
		if (is_HI){
			Log.prnc(2," isHM:"+is_Hi_Mode);
			Log.prn(2,", is_hi_preferred:"+is_hi_preferred);
			
		}else{
			Log.prn(2," isDrop:"+is_dropped);
		}
		
	}
	
	public void prnOffline() {
		Log.prnc(2, "tid:"+tid);
		Log.prnc(2, ", "+MUtil.getStr(getLoUtil()));
		Log.prnc(2, ", "+MUtil.getStr(getHiUtil()));
		if (is_HI){
			Log.prn(2,", is_hi_preferred:"+is_hi_preferred);
			
		}else{
			Log.prn(2,", LO-task ");
		}
		
	}
	public void prnPara() {
		Log.prnc(2, "tmp.add(new Task(0,");
		Log.prnc(2, period);
		if (is_HI){
			Log.prnc(2, ", "+c_l);
			Log.prnc(2, ", "+c_h);
		} else {
			Log.prnc(2, ", "+c_l);
		}
		Log.prn(2, "));");

	}

	public void initMode() {
		is_dropped=false;
		if(is_hi_preferred)
			is_Hi_Mode=true;
		else
			is_Hi_Mode=false;
	}





}

