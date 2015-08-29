package Simul;

public class Task {
	public int tid;
	public int period;
	public int c_l;
	public int c_h;

	public Task(int tid,int period, int c_l, int c_h) {
		this.tid=tid;
		this.period = period;
		this.c_l = c_l;
		this.c_h = c_h;
	}

	public void prn() {
		System.out.println("tid:"+tid+" p:"+period+" e:"+c_l);
	}
}
