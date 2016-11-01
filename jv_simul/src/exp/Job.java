package exp;

import utill.Log;

public class Job implements Comparable<Job>{
	public int tid;
	public int dl;
	public int exec;
	public int add_exec;
	public boolean isHI;


	public double vd;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
		this.vd = dl;
	}

	public Job(int tid,int dl, int exec,double  vd,int add) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
		this.vd = vd;
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
