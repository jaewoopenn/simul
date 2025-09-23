package job;

import util.MCal;
import util.SLog;

// Drop is +2000 VD

public class Job implements Comparable<Job>{
	public int tid;
	public int dl;
	public int exec;
	public int opt;
	public int val;
	public double den;

	public Job(int tid,int dl, int exec,  int value) {
		this(tid,dl,exec,0,value);
	}
	
	public Job(int tid,int dl, int exec, int opt, int val) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
		this.opt=opt;
		this.val=val;
		den=(double)val/(exec+opt);
	}

	public String info() {
		String s="tid:"+tid;
		s+=" ,dl:"+dl;
		s+=" ,exec:"+exec;
		s+=" ,opt:"+opt;
		s+=" ,val:"+val;
		s+=" ,den:"+MCal.getStr(den);
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
