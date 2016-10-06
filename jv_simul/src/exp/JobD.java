package exp;

import utilSim.Log;

public class JobD extends AbsJob implements Comparable<JobD>{
	public double vd;

	public JobD(int tid,int dl, int exec) {
		super(tid,dl,exec);
		this.vd = dl;
	}

	public JobD(int tid,int dl, int exec,double  vd,int add) {
		super(tid,dl,exec,add);
		this.vd = vd;
	}

	public void prn() {
		Log.prnc(1,tid+","+dl+","+exec);
		if(isHI)
			Log.prn(1,","+vd+","+add_exec);
		else
			Log.prn(1,"");
			
	}

	@Override
	public int compareTo(JobD o) {
		double o_vd = o.vd;  
		if (vd>o_vd)
			return 1;
		else if (vd==o_vd)
			return 0;
		else
			return -1;
	}
}
