package Simul;

public class Task {
	public int tid;
	public int cid;
	public int period;
	public double c_l;
	public double c_h;
	public double vd;
	public boolean is_HI;

	public Task(int tid,int period, double c_l) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_l;
		this.is_HI=false;
	}

	public Task(int tid,int period, double c_l, double c_h) {
		this.tid=tid;
		this.period = period;
		this.vd = period;
		this.c_l = c_l;
		this.c_h = c_h;
		this.is_HI=true;
	}

	public void setVD(double vd){
//		System.out.println("tid:"+tid+" vd:"+vd);
		this.vd=vd;
	}
	public void setCom(int c){
		this.cid=c;
	}
	public void prn() {
		System.out.println("tid:"+tid+" p:"+period+" e:"+c_l);
	}
}

