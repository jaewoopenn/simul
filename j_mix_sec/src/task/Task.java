package task;
/*

Individual Task 

*/

import util.SLog;

public class Task {
	public int tid;
	public int period;
	public int c_l;
	public int c_h;
	private boolean is_HC=false;
	

	public Task(int period, int c_l) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.c_l = c_l;
		this.c_h = c_l;
	}

	public Task(int period, int c_l, int c_h) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HC=true;
	}

	
	
	
	
	// get Param
	public boolean isHC() {
		return is_HC;
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
	// prn
	public void prn() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, " p:"+period);
		SLog.prnc(2," cl:"+c_l+" ch:"+c_h);
		if (is_HC){
			SLog.prn(2," HI");
		}else{
			SLog.prn(2," LO");
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
}

