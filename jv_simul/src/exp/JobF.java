package exp;

import utill.Log;

public class JobF extends AbsJob implements Comparable<JobF>{
	public int pri;

	public JobF(int tid,int dl, int exec,int pri) {
		super(tid,dl,exec);
		this.pri=pri;
	}

	public JobF(int tid,int dl, int exec,int pri,int add) {
		super(tid,dl,exec,add);
		this.pri=pri;
	}

	public void prn() {
		Log.prnc(1,tid+","+pri+","+dl+","+exec);
		if(isHI)
			Log.prn(1,","+add_exec);
		else
			Log.prn(1,"");
			
	}

	@Override
	public int compareTo(JobF o) {
		double o_pri = o.pri;  
		if (pri>o_pri)
			return 1;
		else if (pri==o_pri)
			return 0;
		else
			return -1;
	}
}
