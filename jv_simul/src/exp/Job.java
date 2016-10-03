package exp;

import utilSim.Log;

public class Job implements Comparable<Job>{
	public int tid;
	public double vd;
	public int dl;
	public int exec;
	public int add_exec;
	public boolean isHI;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.vd = dl;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
	}

	public Job(int tid,int dl, int exec,double  vd,int add) {
		this.tid=tid;
		this.vd = vd;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
	}

	public void prn() {
		Log.prnc(1,tid+","+dl+","+exec);
		if(isHI)
			Log.prn(1,","+vd+","+add_exec);
		else
			Log.prn(1,"");
			
	}

	@Override
	public int compareTo(Job o) {
		double o_vd = o.vd;  
		if (vd>o_vd)
			return 1;
		else if (vd==o_vd)
			return 0;
		else
			return -1;
	}
}
