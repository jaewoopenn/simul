package Exp;

import Util.Log;

public class Job implements Comparable{
	public int tid;
	public double vd;
	public int dl;
	public double exec;
	public double add_exec;
	public boolean isHI;

	public Job(int tid,int dl, double exec) {
		this.tid=tid;
		this.vd = dl;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
	}

	public Job(int tid,double vd, int dl, double exec,double add) {
		this.tid=tid;
		this.vd = vd;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
	}

	@Override
	public int compareTo(Object o) {
		double o_vd = ((Job)o).vd;  
		if (vd>o_vd)
			return 1;
		else if (vd==o_vd)
			return 0;
		else
			return -1;
	}
	
	public void prn() {
		Log.prn(1,tid+","+dl+","+exec);
	}
}
