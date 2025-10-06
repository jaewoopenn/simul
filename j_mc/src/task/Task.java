package task;
/*

Individual Task 

*/


//import util.SLog;
//import util.MCal;
import util.SLogF;

public class Task {
	public int tid;
	public int period;
	public int c_l;   // ac
	public int c_h;   // de
	public double vd;
	private int status=0; // 0: normal, 1: new, 2: removed
	protected boolean is_HC=false;
	protected boolean is_HI_Mode=false;
//	protected boolean is_MS_Mode=false;
	protected boolean is_dropped=false;
	protected boolean is_hi_preferred=false;
	

	public Task(int period, int c_l, int c_h,boolean is_HC) {
		this(TaskSeq.getID(),period,c_l,c_h,is_HC);
	}

	
	public Task(int tid, int period, int c_l, int c_h,boolean is_HC) {
		this.tid=tid;
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
	public double getLIKE(){
		return (double)(c_l-c_h);
	}
	public double getHiUtil(){
		return (double)c_h/period;
	}
	public double getLoVdUtil(){
		return (double)c_l/vd;
	}
	
	public double getMaxUtil() {
		return (double)Math.max(c_l, c_h)/period;
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
//		SLogF.prn("drop "+tid);
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
//	public boolean isMS() {
//		return is_MS_Mode;
//	}
	public boolean isDrop() {
		return is_dropped;
	}

	public boolean isHI_Preferred() {
		return is_hi_preferred;
	}



	//set Param
	public void setVD(double x){
		this.vd=x*this.period;
	}
	public void setHI_only() {
		is_hi_preferred = true;
		this.vd=this.period;
	}
	public void setNormal(double x) {
		is_hi_preferred = false;
		this.vd=x*this.period;
	}


	public Task copy() {
		Task t=new Task(tid, period, c_l, c_h, is_HC);
		return t;
	}


	public void markRemoved() {
		status=2;
	}
	public void markNew() {
		status=1;
	}


	public boolean removed() {
		return status==2;
	}
	public boolean is_new() {
		return status==1;
	}


	public void reset() {
		status=0;
		
	}




}

