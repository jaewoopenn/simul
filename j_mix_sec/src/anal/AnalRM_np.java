package anal;

import java.util.Vector;


import task.Task;
import task.TaskSet;
import util.SLog;

public class AnalRM_np extends Anal {
	private TaskSet g_tasks;
	private int g_sort;
	public AnalRM_np(int i) {
		super();
		g_sort=i;
		if(g_sort==0) {
			g_name="RM_np";
		} else {
			g_name="CM_np";
		}
	}
	@Override
	public void prepare() {
		g_tasks=new TaskSet(g_tm.getTasks());
		if(g_sort==0) {
			g_tasks.sortRM();
		} else {
			g_tasks.sortCM();
			
		}
//		g_tasks.prn();
	}

	
	@Override
	public double getDtm() {
		SLog.prn(1, "RM");
		if (check())
			return 0.5;
		else
			return 2;
	}





	private boolean check()
	{
		for(int i=0;i<g_tasks.size();i++) {
			Task t=g_tasks.get(i);
			Task[] hp=getHP(i);
			Task[] lp=getLP(i);
			double res=computeRes( t,  hp,lp);
			SLog.prn(1, i+" hp: "+hp.length+" lp: "+lp.length);		
			SLog.prn(1, i+" "+res+" "+t.period);		
			if(res>t.period)
				return false;
		}
		return true;
	}
	
	private Task[] getHP(int p){ 
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<g_tasks.size();i++)
		{
//			SLog.prn(1, "prio"+i+" "+prio[i]);
			if(i<p)
				v.add(g_tasks.get(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
	}
	private Task[] getLP(int p){ 
		Vector<Task> v=new Vector<Task>();
		for(int i=0;i<g_tasks.size();i++)
		{
//			SLog.prn(1, "prio"+i+" "+prio[i]);
			if(i>p)
				v.add(g_tasks.get(i));
		}
		Task[] ret=new Task[v.size()];
		v.toArray(ret);
		return ret;
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

	
	@Override
	public void prn() {
		for(int i=0;i<g_tasks.size();i++) {
			Task t=g_tasks.get(i);
			Task[] hp=getHP(i);
			Task[] lp=getLP(i);
			double res_lo=computeRes(t,hp,lp);
			SLog.prn(1, ""+res_lo+" "+t.period);
		}		
	}

}
