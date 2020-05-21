package task;
/*

Individual Task 

*/


import util.SLog;
import util.MCal;
import util.SLogF;

public class Task {
	public int tid;
	public int period;
	public int c_l;
	public int c_h;
	public double vd;
	public int sb_tm=-1; // switch back time
	private boolean is_HC=false;
	private boolean is_HI_Mode=false;
	private boolean is_dropped=false;
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
		this.is_HC=true;
	}
	public void ms(){
		if(!is_hi_preferred)
			is_HI_Mode=true;
	}
	public void drop() {
		SLogF.prn("drop "+tid);
		this.is_dropped=true;
	}
	public void resume() {
		SLogF.prn("resume "+tid);
		this.is_dropped=false;
	}
	public void setVD(double vd){
//		System.out.println("tid:"+tid+" vd:"+vd);
		this.vd=vd;
	}
	public void setHI_only() {
		 is_hi_preferred = true;
		 is_HI_Mode=true;
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
	public double getLoUtil2(){
		return (double)c_l;
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
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, " p:"+period);
		if (is_HC){
			SLog.prnc(2," cl:"+c_l+" ch:"+c_h+" vd:"+vd);
			SLog.prnc(2," isHM:"+is_HI_Mode);
			
		}else{
			SLog.prnc(2," cl:"+c_l);
			SLog.prnc(2," isDrop:"+is_dropped);
		}
		SLog.prnc(2," hi-crit?"+is_HC);
		SLog.prn(2," util:"+getLoUtil());
	}
	public void prnShort() {
		SLog.prnc(2, tid);
		SLog.prnc(2, ", "+period);
		SLog.prnc(2, ", "+c_l);
		SLog.prnc(2, ", "+c_h);
		if (is_HC)
			SLog.prn(2,",H");
		else
			SLog.prn(2,",L");
	}
	
	public void prnTxt() {
		SLog.prnc(2, ""+period);
		SLog.prnc(2, ","+c_l);
		SLog.prnc(2, ","+c_h);
		if (is_HC)
			SLog.prn(2,",1");
		else
			SLog.prn(2,",0");
	}
	
	
	public void prnRuntime() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, ", "+MCal.getStr(getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(getHiUtil()));
		if (is_HC){
			SLog.prnc(2," isHM:"+is_HI_Mode);
			SLog.prn(2," isHI_Only:"+is_hi_preferred);
			
		}else{
			SLog.prn(2," isDrop:"+is_dropped);
		}
		
	}
	public void prnStat() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, ", "+MCal.getStr(getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(getHiUtil()));
		if (is_HC){
			SLog.prnc(2," isHM:"+is_HI_Mode);
			SLog.prn(2,", is_hi_preferred:"+is_hi_preferred);
			
		}else{
			SLog.prn(2," isDrop:"+is_dropped);
		}
		
	}
	
	public void prnOffline() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, ", "+MCal.getStr(getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(getHiUtil()));
		if (is_HC){
			SLog.prn(2,", is_hi_preferred:"+is_hi_preferred);
			
		}else{
			SLog.prn(2,", LO-task ");
		}
		
	}
	public void prnPara() {
		SLog.prnc(2, "tmp.add(new Task(0,");
		SLog.prnc(2, period);
		if (is_HC){
			SLog.prnc(2, ", "+c_l);
			SLog.prnc(2, ", "+c_h);
		} else {
			SLog.prnc(2, ", "+c_l);
		}
		SLog.prn(2, "));");

	}

	public void initMode() {
		is_dropped=false;
		if(is_hi_preferred)
			is_HI_Mode=true;
		else
			is_HI_Mode=false;
		sb_tm=-1;
	}
	
	// get Param
	public boolean isHC() {
		return is_HC;
	}
	public boolean isHM() {
		return is_HI_Mode;
	}
	public boolean isDrop() {
		return is_dropped;
	}

	public boolean isHI_Preferred() {
		return is_hi_preferred;
	}






}

