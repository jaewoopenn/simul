package basic;



import util.S_Log;

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
		S_Log.prnc(2, "tid:"+tid);
		S_Log.prnc(2, " p:"+period);
		S_Log.prnc(2, " p:"+exec);
	}
	

}

