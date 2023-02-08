package anal;

import java.util.Vector;


import task.Task;
import util.SLog;

public class AnalSMC_np2 extends Anal {
	private int sz;
	private int[] prio;
	public AnalSMC_np2() {
		super();
		g_name="SMC_np2";
	}
	@Override
	public void prepare() {
		sz=g_tm.getTasks().length;
		prio=new int[sz];
	}
	
	@Override
	public double getDtm() {
		SLog.prn(1, "SMC_np2");
//		g_tm.prnPara();
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
				SLog.prn(1, "checking prio:"+i+" len:"+ts.length);
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
		double res1=computeRes(t,hp,lp);
		double res2=computeRes2(t,hp,lp);
		SLog.prn(1, "res "+res1+" "+res2);
		return Math.max(res1, res2)<=t.period;
	}

	private double computeRes(Task t, Task[] hp,Task[] lp) {
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
		if(t.isHC())
			init_res=b+t.c_h;
		else
			init_res=b+t.c_l;
		return computeResHP(t,hp,init_res);
	}

	private double computeResHP(Task t, Task[] hp,double init_res) {
		double res=0;
		double old_res=0;
		while(res<=t.period){
			old_res=res;
			res=init_res;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk) continue;
				res+=Math.ceil((double)old_res/h_tsk.period)*h_tsk.c_l;			}
//			SLog.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
		}
		return res;
		
	}
	private double computeRes2(Task t, Task[] hp,Task[] lp) {
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
		init_res=b+t.c_l;
		if(!t.isHC()) {
			return computeResHP(t,hp,init_res);
		}
		
		double res=0;
		double old_res=0;
			
		while(res<=t.period){
			old_res=res;
			res=init_res;
			for(int i=0;i<hp.length;i++){
				double att=init_res;
				Task a_tsk=hp[i];
				if(t==a_tsk) 	continue;
				if(!a_tsk.isHC()) continue;
				att+=Math.ceil((double)old_res/a_tsk.period)*a_tsk.c_h;
				for(int k=0;k<hp.length;k++){
					Task h_tsk=hp[k];
					if(t==h_tsk) continue;
					if(i==k) continue;
					att+=Math.ceil((double)old_res/h_tsk.period)*h_tsk.c_l;
				}
				res=Math.max(att,res);
				SLog.prn(1, "att "+att+" "+res);
			}
			SLog.prn(1, "r/o "+res+" "+old_res);
			if(res==old_res) break;
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
			SLog.prn(1,"task "+i+", prio: "+prio[i]);
		}
		for(int i=0;i<sz;i++) {
			Task t=g_tm.getTask(i);
			Task[] hp=getHP(prio[i]);
			Task[] lp=getLP(prio[i]);
			double res_lo=computeRes(t,hp,lp);
			SLog.prn(1, ""+res_lo+" "+t.period);
		}
	}

}
