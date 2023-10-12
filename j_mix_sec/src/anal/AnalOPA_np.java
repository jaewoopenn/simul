package anal;

import java.util.Vector;

// IMprecise version

import task.Task;
import util.SLog;

//
// drop 안하는 것. 
// down sacled 이것은 약 1/2 exec. time
// 

public class AnalOPA_np extends Anal {
	private int sz;
	private int[] prio;
	public AnalOPA_np() {
		super();
		g_name="OPA_np"; // AMC-rtb
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




	

	private boolean findOPA()
	{
		boolean b;
		for(int p=sz;p>0;p--)
		{
			SLog.prn(1, "assigning "+p);
			Task[] ts=getUnprio();
			Task[] l_ts=getPrio();
			b=false;
			for(int i=0;i<sz;i++){
				Task t=g_tm.getTask(i);
				if(prio[i]!=0) continue;
				SLog.prn(1, "checking "+i+" "+ts.length);
				if(chk_A_on_setB(t,ts,l_ts)){
					prio[i]=p;
					b=true;
					break;
				}
			}
			if(!b) return false;
		}
		return true;
	}
	
	private boolean chk_A_on_setB(Task t, Task[] hp,Task[] lp) {
		double res_lo=computeRes(t,hp,lp);
		return res_lo<=t.deadline;
	}

	private double computeRes(Task t, Task[] hp, Task[] lp) {
		double res=0;
		double init_res;
		double b=0;
		for(Task lt:lp) {
			double exec=0;
			if(lt.isHC())
				exec=lt.c_h;
			else
				exec=lt.c_l;
			b=Math.max(b, exec);
		}
		SLog.prn(1, "blocking "+b);
		if(t.isHC())
			init_res=b+t.c_h;
		else
			init_res=b+t.c_l;
		double old_res=0;
		double exec=0;
		while(res<=t.period){
			old_res=res;
			res=init_res;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)		continue;
				if(h_tsk.isHC())
					exec=h_tsk.c_h;
				else
					exec=h_tsk.c_l;
				res+=Math.ceil((double)old_res/h_tsk.period)*exec;
			}
//			SLog.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
		}
//		SLog.prn(1, "r "+t.tid+" "+hp.length);
		return res;
	}

	private Task[] getUnprio(){ // get un prioritied task 
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
	private Task[] getPrio(){ 
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<sz;i++)
		{
//			Log.prn(1, "prio"+i+" "+prio[i]);
			if(prio[i]!=0)
				v.add(g_tm.getTask(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}	
	private Task[] getHP(int p){ 
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<sz;i++)
		{
//			Log.prn(1, "prio"+i+" "+prio[i]);
			if(prio[i]<p)
				v.add(g_tm.getTask(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}
	private Task[] getLP(int p){ 
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<sz;i++)
		{
//			Log.prn(1, "prio"+i+" "+prio[i]);
			if(prio[i]>p)
				v.add(g_tm.getTask(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}
	
	@Override
	public void prn() {
	}

}
