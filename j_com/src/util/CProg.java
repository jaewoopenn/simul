package util;

public class CProg {
	private int max=0;
	private int step=1;
	private int cur=0;
	private int log=1;
	public CProg(int n) {
		max=n;
	}
	public void setLog(int n) {
		log=n;
	}
	public void setPercent() {
		int d=max/100;
		setStep(d);
	}
	public void setStep(int n) {
		step=n;
	}
	public void inc() {
		cur++;
		if(cur%step==0) {
			prn();
		}
	}
	public void reset() {
		cur=0;
	}
	public void prn() {
		SLog.prn(log,((double)cur)/max+"");
	}

}
