package basic;

import utilSim.Log;

public class Task {
	public int tid;
	public int cid;
	public int period;
	public int c_l;
	public int c_h;
	public double vd;
	public boolean is_HI;
	public boolean is_HM;
	public boolean is_dropped;

	public Task(int tid,int period, int c_l) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_l;
		this.is_HI=false;
		this.is_HM=false;
		this.is_dropped=false;
	}

	public Task(int tid,int period, int c_l, int c_h) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HI=true;
		this.is_HM=false;
		this.is_dropped=false;
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
	
	public void prn() {
		if (is_HI){
			Log.prn(2, "tid:"+tid+" (t,cl,ch): "+
					period+","+c_l+","+c_h+
					" hi-crit?"+is_HI+" VD:"+vd);
			
		}else{
			Log.prn(2, "tid:"+tid+" (t,cl): "+
					period+","+c_l+
					" hi-crit?"+is_HI);
			
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

}

