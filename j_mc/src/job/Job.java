package job;

import util.SLog;

// Drop is +2000 VD

public class Job implements Comparable<Job>{
	public int tid;
	public int rl;
	public int dl;
	public int exec;
	public int add_exec;
	public boolean isHI;
	public boolean isMS=false;
	public int vd;
	private boolean isDrop;

	public Job(int tid,int rl, int dl, int exec) {
		this.tid=tid;
		this.rl=rl;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=0;
		this.isHI=false;
		this.isDrop=false;
		this.vd = dl;
	}
	public Job(int tid,int rl, int dl, int exec,int add) {
		this.tid=tid;
		this.rl=rl;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=false;
		this.isDrop=false;
		this.vd = dl;
	}

	public Job(int tid,int rl, int dl, int exec, int  vd,int add) {
		this.tid=tid;
		this.rl=rl;
		this.dl = dl;
		this.exec = exec;
		this.add_exec=add;
		this.isHI=true;
		this.isDrop=false;
		this.vd = vd;
	}
	public void ms() {
		exec+=add_exec;
		add_exec=0;
		isMS=true;
		
	}
	public void degrade() {
		if(isHI) 
			SLog.err("Job:cannot degrade HC task");
		if(isDrop)
			return;
		isDrop=true;
		exec-=add_exec;
		add_exec=0;
		if(exec<0)
			exec=0;
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
		String s="tid: "+tid;
		s+=", rl:"+rl;			
		if(isHI) {
			s+=", HC";
			s+=", vd:"+vd;			
		} else {
			s+=", LC";

		}
		s+=", dl:"+dl+", exec:"+exec;
		if(add_exec!=0) {
			s+=", add:"+add_exec;
		}
		if(isDrop) {
			s+=", DROPPED";
		}
		if(isMS) {
			s+=", MS";
		}
		return s;
			
	}

	public boolean isDrop() {
		return isDrop;
	}

	public void setVD(int d) {
		this.vd=d;
	}
	public void prn() {
		SLog.prn(1, info());
		
	}


}
