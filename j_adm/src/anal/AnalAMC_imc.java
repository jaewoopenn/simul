package anal;

import java.util.Vector;

import task.Task;
import util.SLog;

public class AnalAMC_imc extends Anal {
	private int sz;
	private int[] prio;
	public AnalAMC_imc() {
		super();
		g_name="AMC-IMC";
	}
	
	@Override
	protected void prepare() {
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
				if(prio[i]!=0) 
					continue;
				SLog.prn(1, "checking "+i+" "+ts.length);
				if(chk_A_on_setB(t,ts)){
					prio[i]=p;
					b=true;
					break;
				}
			}
			if(!b) return false;
		}
		return true;
	}
	
	private boolean chk_A_on_setB(Task t, Task[] hp) {
		if(t.isHC()) {
			double res_lo=computeLO(t,hp);
			double res=computeHI(t,hp,res_lo);
			SLog.prn(1, ""+res+" "+t.period);
			return Math.max(res_lo,res)<=t.period;
		} else {
			double res_lo=computeLOs(t,hp);
			double res=computeHI(t,hp,res_lo);
			SLog.prn(1, ""+res_lo+" "+res+" "+t.period);
			return Math.max(res_lo,res)<=t.period;
		}
	}

	private double computeLO(Task t, Task[] hp) {
		double res=t.c_l;
		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=t.c_l;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)
					continue;
				exec=h_tsk.c_l;
				res+=Math.ceil((double)old_res/h_tsk.period)*exec;
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
	private double computeLOs(Task t, Task[] hp) {
		double res=t.c_h;
		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=t.c_l;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)
					continue;
				exec=h_tsk.c_l;
				res+=Math.ceil((double)old_res/h_tsk.period)*exec;
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
	private double computeHI(Task t, Task[] hp, double res_lo) {
		double res=t.c_h;
		
		double old_res=0;
		double exec=0;
		while(true){
			old_res=res;
			res=t.c_h;
			for(int i=0;i<hp.length;i++){
				Task h_tsk=hp[i];
				if(t==h_tsk)
					continue;
				exec=h_tsk.c_h;
				res+=Math.ceil((double)old_res/h_tsk.period)*exec;
				if(!h_tsk.isHC()) { // LC
					exec=h_tsk.c_l-h_tsk.c_h;
					res+=Math.ceil((double)res_lo/h_tsk.period)*exec;
					
				}
			}
//			if(Double.isNaN(res)||res>1000) {
//				SLog.prn(1, "!!r/o "+res+" "+old_res);
//				System.exit(1);
//			}
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
	private Task[] getHP(int p){ // get un prioritied task 
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
	
	@Override
	public void prn() {
		for(int i=0;i<sz;i++) {
			SLog.prn(1,i+" "+prio[i]);
		}
		for(int i=0;i<sz;i++) {
			Task t=g_tm.getTask(i);
			Task[] hp=getHP(prio[i]);
			if(t.isHC()) {
				double res_lo=computeLO(t,hp);
				double res=computeHI(t,hp,res_lo);
				SLog.prn(1, ""+res+" "+t.period);
			} else {
				double res_lo=computeLOs(t,hp);
				double res=computeHI(t,hp,res_lo);
				SLog.prn(1, ""+res_lo+" "+res+" "+t.period);
			}

		}
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void setX(double x) {
	}

}
