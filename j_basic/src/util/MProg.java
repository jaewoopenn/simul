package util;

public class MProg {
	private int max=0;
	private int step=1;
	private int cur=0;
	private int log=1;
	private boolean is_verbose=false;
	public MProg(int n) {
		max=n;
	}
	public void setLog(int n) {
		log=n;
	}
	public void setVerbose(int s) {
		is_verbose=true;
		setStep(s);
	}
	public void setPercent() {
		int d=max/100;
		setStep(d);
	}
	private void setStep(int n) {
		step=n;
	}
	public void inc() {
		cur++;
		if(step==0) {
			prn();
			return;
		}
		if(cur%step==0) {
			prn();
		}
		if(cur%(step*20)==0) {
			SLog.prn(log,"");
		}
	}
	public void reset() {
		cur=0;
	}
	public void prn() {
		if(is_verbose) {
			SLog.prnc(log,cur+"/"+max+" ");
		} else {
			SLog.prnc(log,(int)(((double)cur)/max*100)+" ");
		}
	}
	public static MProg init(int i) {
		return new MProg(i);
	}

}
