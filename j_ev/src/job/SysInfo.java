package job;

import util.SLog;

public class SysInfo {
	public int total=0;
	public int suc=0;
	public void prn() {
		double p=(double)suc/total;
		SLog.prn(2,"suc:"+p);
		
	}

}
