package job;

import util.MCal;
import util.SLog;

public class JobInput implements Comparable<JobInput> {
	public int dl;
	public int exec;
	public int val;
	public double den;
	public JobInput(int dl, int exec, int value) {
		this.dl = dl;
		this.exec = exec;
		this.val=value;
		den=(double)val/exec;
	}
	public String info() {
		String s="dl:"+dl;
		s+=" ,exec:"+exec;
		s+=" ,val:"+val;
		s+=" ,den:"+MCal.getStr(den);
		return s;
			
	}

	public void prn() {
		SLog.prn(1, info());
		
	}

	@Override
	public int compareTo(JobInput o) {
		double o_d = o.den;  
		if (den>o_d)
			return -1;
		else if (den==o_d)
			return 0;
		else
			return 1;
	}

}
