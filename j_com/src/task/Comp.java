package task;



import util.SLog;

public class Comp {
	public int cid;
	public int period;
	public int exec;
	private TaskSet ts;
	

	public Comp(int id,int period, int c) {
		this.cid=id;
		this.period = period;
		this.exec=c;
	}
	public void setTS(TaskSet t) {
		ts=t;
	}
	public void setTV(TaskVec tv) {
		ts=new TaskSet(tv);
		
	}
	public TaskSet getTS() {
		return ts;
	}

	
	public void prn() {
		SLog.prnc(2, "cid:"+cid);
		SLog.prnc(2, " T:"+period);
		SLog.prn(2, " C:"+exec);
	}
	public double getUtil() {
		double u=0;
		for(Task t:ts.getArr()) {
			u+=t.getUtil();
		}
		return u;
	}
	

}

