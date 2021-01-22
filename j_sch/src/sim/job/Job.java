package sim.job;

import util.SLog;

// Drop is +2000 VD

public class Job implements Comparable<Job>{
	public int tid;
	public int dl;
	public int exec;
	public int add_exec;
	public boolean isHI;
	public double vd;
	private boolean isDrop;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
		this.isDrop=false;
		this.vd = dl;
	}

	public Job(int tid,int dl, int exec,double  vd,int add) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
		this.isDrop=false;
		this.vd = vd;
	}

	public void drop() {
		if(isHI) 
			SLog.err("Job:cannot drop HC task");
		if(isDrop)
			return;
		isDrop=true;
		this.vd+=2000;   
//		SLogF.prn("drop "+this.tid+","+this.exec+","+this.vd);
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

	public String info() {
		String s="tid:"+tid+",isHI:"+isHI+",dl:"+dl+",exec:"+exec;
		s+=",vd:"+vd+",add:"+add_exec;
		return s;
			
	}

	public boolean isDrop() {
		return isDrop;
	}

	public void setVD(int d) {
		this.vd=d;
	}


}
