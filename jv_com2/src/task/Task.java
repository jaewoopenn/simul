package task;
/*

Individual Task 

*/


import util.SLog;
import util.MCal;
import util.SLogF;

public class Task {
	public int tid;
	public int cid;
	public int period;
	public int c_l;
	public int c_h;
	public double vd;
	public double x;
	public int life=0;
	private boolean is_isol=false;
	private boolean is_HC=false;
	private boolean is_HI_Mode=false;
	private boolean is_dropped=false;
	private boolean is_hi_preferred=false;
	
	public Task() {
		this.tid=TaskSeq.getID();
	}
	public Task(int period, int c_l) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = 0;
	}

	public Task(int period, int c_l, int c_h) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HC=true;
	}
	public Task(int period, int c_l, int c_h,boolean is_HC) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HC=is_HC;
	}

	
	public boolean check() {
		if (period==0)
			return false;
		if (c_l==0)
			return false;
		return true;
	}
	
	public double getLoUtil(){
		return (double)c_l/period;
	}
	public double getLoExec(){
		return (double)c_l;
	}
	public double getHiUtil(){
		return (double)c_h/period;
	}
	public double getLoVdUtil(){
		return (double)c_l/vd;
	}
	public double getMsUtil(){
		return (double)(c_h-c_l)/(period-vd);
	}
	

	
	public void prn() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, " p:"+period);
		if (is_HC){
			SLog.prnc(2," cl:"+c_l+" ch:"+c_h+" vd:"+vd);
			SLog.prnc(2," mode:"+is_HI_Mode);
			
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
		if (is_isol)
			SLog.prnc(2,",I");
		else
			SLog.prnc(2,",S");
		if (is_HC)
			SLog.prn(2,",H");
		else
			SLog.prn(2,",L");
	}
	
	public void prnTxt() {
		SLog.prnc(2, "p:"+period);
		SLog.prnc(2, ",vd:"+vd);
		SLog.prnc(2, ",cl:"+c_l);
		SLog.prnc(2, ",ch:"+c_h);
		if (is_hi_preferred)
			SLog.prnc(2,",HO");
		else
			SLog.prnc(2,",N");
		if (is_HC)
			SLog.prn(2,", HC");
		else
			SLog.prn(2,", LC");
	}
	
	
	public void prnRuntime() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, ", "+MCal.getStr(getLoUtil()));
		SLog.prnc(2, ", "+MCal.getStr(getHiUtil()));
		if (is_HC){
			SLog.prn(2," isHM:"+is_HI_Mode);
//			SLog.prn(2," isHI_Only:"+is_hi_preferred);
			
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
		SLog.prnc(2, "tmp.add(new Task(");
		SLog.prnc(2, period);
		if (is_HC){
			SLog.prnc(2, ", "+c_l);
			SLog.prnc(2, ", "+c_h);
		} else {
			SLog.prnc(2, ", "+c_l);
		}
		SLog.prn(2, "));");

	}

	// operation
	public void initMode() {
		is_dropped=false;
		if(is_hi_preferred)
			is_HI_Mode=true;
		else
			is_HI_Mode=false;
	}

	public void ms(){
		if(is_hi_preferred) 
			return;
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

	public boolean isHO() {
		return is_hi_preferred;
	}



	//set Param
	public void setX(double x){
		this.x=x;
		if(x>1) {
			SLog.err("VD set error x>1:"+x);
		}
		this.vd=x*this.period;
	}
	public void setHI_only() {
		 is_hi_preferred = true;
		 is_HI_Mode=true;
	}
	public void setNormal() {
		 is_hi_preferred = false;
		 is_HI_Mode=false;
	}

	public void setComp(int id) {
		cid=id;
	}

	public int getComp() {
		return cid;
	}

	public void set_isol(boolean b) {
		is_isol=b;
	}
	public boolean is_isol() {
		return is_isol;
	}



}

