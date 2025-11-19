package job;

import util.SLog;

// Drop is +2000 VD

public class Job implements Comparable<Job>{
	public int tid;
	public int dl;
	public int exec;
	public int rel_dl;
	public double util=0;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
	}

	public String info() {
		String s="tid:"+tid;
		s+=" ,dl:"+dl;
		s+=" ,exec:"+exec;
		return s;
			
	}

	public void prn() {
		SLog.prn(1, info());
		
	}
	@Override
	public int compareTo(Job o) {
		double o_d = o.dl;  
		if (dl>o_d)
			return 1;
		else if (dl==o_d)
			return 0;
		else
			return -1;
	}


}
