package Simul;

import Util.Log;

public class Job implements Comparable{
	public int tid;
	public int dl;
	public int exec;

	public Job(int tid,int dl, int exec) {
		this.tid=tid;
		this.dl = dl;
		this.exec = exec;
	}
	@Override
	public int compareTo(Object o) {
		int o_dl = ((Job)o).dl;  
		return dl - o_dl; 
	}
	public void prn() {
		Log.prn(1,tid+","+dl+","+exec);
	}
}
