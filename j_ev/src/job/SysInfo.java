package job;

import util.SLog;

public class SysInfo {
	public int total=0;
	public int suc=0;
	public int val=0;
	public void prn() {
		SLog.prn(2,"suc:"+getSuc());
		SLog.prn(2,"val:"+val);
		
	}
	public double getSuc() {
		double p=(double)suc/total;
		return p;
	}

}
