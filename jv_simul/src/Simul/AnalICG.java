package Simul;

import java.util.Vector;

import Util.Log;

public class AnalICG extends Anal {
	private int sz;
	private int[] prio;
	@Override
	public void prepare() {
		sz=tm.size();
		prio=new int[sz];
	}
	
	@Override
	public boolean isScheduable() {
		Log.prn(1, "OPA");
		return findOPA();
	}

	@Override
	public int getResp() {
		return 0;
	}

	@Override
	public double getDropRate(double prob_hi) {
		return 0;
	}

	private boolean findOPA()
	{
		boolean b;
		for(int p=sz;p>0;p--)
		{
			Log.prn(1, "assigning "+p);
			Task[] ts=getUnprio();
			b=false;
			for(int i=0;i<sz;i++){
				Task t=tm.getTask(i);
				if(prio[i]!=0) continue;
				Log.prn(1, "checking "+i+" "+ts.length);
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
		int res=t.c_h;
		
		int old_res=0;
		int exec=0;
		while(true){
			old_res=res;
			res=t.c_h;
			for(int i=0;i<ts.length;i++){
				Task hp=ts[i];
				if(t==hp)
					continue;
				if(t.is_HI)
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
				v.add(tm.getTask(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}
	


}