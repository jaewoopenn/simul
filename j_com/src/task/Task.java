package task;



import util.SLog;

public class Task {
	public int tid;
	public int period;
	public int exec;

	

	public Task(int period, int c) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.exec=c;
	}

	public double getUtil(){
		return (double)exec/period;
	}
	public Task getCopy() {
		Task t=new Task(period, exec);
		return t;
	}
	
	public void prn() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, " T:"+period);
		SLog.prn(2, " C:"+exec);
	}
	

}

