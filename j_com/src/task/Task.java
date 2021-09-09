package task;



import util.SLog;

public class Task {
	public int tid;
	public int period;
	public int deadline;
	public double exec;

	

	public Task(int period, double c, int d) {
		this.tid=TaskSeq.getID();
		this.period = period;
		this.exec=c;
		this.deadline=d;
	}

	public double getUtil(){
		return (double)exec/period;
	}
	public Task getCopy() {
		Task t=new Task(period, exec,deadline);
		return t;
	}
	
	public void prn() {
		SLog.prnc(2, "tid:"+tid);
		SLog.prnc(2, " T:"+period);
		SLog.prnc(2, " C:"+exec);
		SLog.prn(2, " D:"+deadline);
	}
	

}

