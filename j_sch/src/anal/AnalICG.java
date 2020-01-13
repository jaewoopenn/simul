package anal;

import java.util.Vector;

import task.Task;
import util.SLog;

public class AnalICG extends Anal {
	private int sz;
	private int[] prio;
	public AnalICG() {
		super();
		g_name="ICG";
	}
	@Override
	public void prepare() {
		sz=g_tm.getTasks().length;
		prio=new int[sz];
	}
	
	@Override
	public double getDtm() {
		SLog.prn(1, "OPA");
		if (findOPA())
			return 0.5;
		else
			return 2;
	}




	@Override
	public double computeX() {
		return 0;
	}
	

	private boolean findOPA()
	{
		boolean b;
		for(int p=sz;p>0;p--)
		{
			SLog.prn(1, "assigning "+p);
			Task[] ts=getUnprio();
			b=false;
			for(int i=0;i<sz;i++){
				Task t=g_tm.getTask(i);
				if(prio[i]!=0) continue;
				SLog.prn(1, "checking "+i+" "+ts.length);
				if(chk(t,ts)){
					prio[i]=p;
					b=true;
					break;
				}
			}
			if(!b) return false;
		}
		return true;
	}
	private boolean chk(Task t, Task[] ts) {
		double res=t.c_h;
		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=t.c_h;
			for(int i=0;i<ts.length;i++){
				Task hp=ts[i];
				if(t==hp)
					continue;
				if(t.isHC())
					exec=hp.c_h;
				else
					exec=hp.c_l;
				res+=Math.ceil((double)old_res/hp.period)*exec;
			}
//			Log.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
		}
		return res<=t.period;
	}

	private Task[] getUnprio(){
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<sz;i++)
		{
//			Log.prn(1, "prio"+i+" "+prio[i]);
			if(prio[i]==0)
				v.add(g_tm.getTask(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}
	@Override
	public void prn() {
		
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}

}
