package anal;

import java.util.Vector;

// IMprecise version

import task.Task;
import util.SLog;

//
// drop 안하는 것. 
// down sacled 이것은 약 1/2 exec. time
// 

public class AnalAMC_np2 extends Anal {
	private int sz;
	private int[] prio;
	public AnalAMC_np2() {
		super();
		g_name="AMC_np2"; // AMC-rtb
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
		double res_lo=computeLO(t,hp,lp);
		if(t.isHC()) {
			double res=computeHI(t,hp,lp,res_lo);
			SLog.prn(1, ""+res+" "+t.period);
			return res<=t.period;
		} else { // LO task 
			SLog.prn(1, ""+res_lo+" "+t.period);
			return res_lo<=t.period;
		}
	}

	private double computeLO(Task t, Task[] hp,Task[] lp) {
		double b=0;
		for(Task lt:lp) {
			double exec=lt.c_l;
			b=Math.max(b, exec);
		}		
		double res=b+t.c_l;
		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=b+t.c_l;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)
					continue;
				exec=h_tsk.c_l;
				res+=Math.ceil((double)old_res/h_tsk.period)*exec;
			}
//			Log.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
			if(res>t.period) {
				res=t.period+1;
				break;
			}
		}
		return res;
	}
	private double computeHI(Task t, Task[] hp, Task[] lp, double res_lo) {
		double b=0;
		for(Task lt:lp) {
			double exec=0;
			if(lt.isHC())
				exec=lt.c_h;
			else
				exec=lt.c_l;
			b=Math.max(b, exec);
		}		
		double res=b+t.c_l;

		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=b+t.c_h;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)
					continue;
				if(h_tsk.isHC()) {
					exec=h_tsk.c_h;
					res+=Math.ceil((double)old_res/h_tsk.period)*exec;
				} else { // LC
					exec=h_tsk.c_l;
					res+=Math.ceil((double)res_lo/h_tsk.period)*exec;
					res+=Math.ceil((double)(old_res-res_lo)/h_tsk.period)*exec/2;
					
				}
			}
//			SLog.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
			if(res>t.period) {
				res=t.period+1;
				break;
			}
		}
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
		for(int i=0;i<sz;i++) {
			SLog.prn(1,i+" "+prio[i]);
		}
		for(int i=0;i<sz;i++) {
			Task t=g_tm.getTask(i);
			Task[] hp=getHP(prio[i]);
			Task[] lp=getLP(prio[i]);
			double res_lo=computeLO(t,hp,lp);
			if(t.isHC()) {
				double res=computeHI(t,hp,lp,res_lo);
				SLog.prn(1, ""+res_lo+" "+res+" "+t.period);
			} else {
				SLog.prn(1, ""+res_lo+" "+t.period);
			}		
		}
	}

}
