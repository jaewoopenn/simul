package Exp;

import Util.Log;

public class Job implements Comparable{
	public int tid;
	public int dl;
	public double vd;
	public int exec;
	public int add_exec;
	public boolean isHI;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.vd = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
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
