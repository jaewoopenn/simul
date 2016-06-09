package Simul;

import java.util.Vector;

import Util.Log;

public class TaskGen {
	private TaskGenParam g_param;
	private Vector<Task> g_tasks;
	private boolean g_isMC=false;
	
	public TaskGen() {
		g_param=new TaskGenParam();
	}

	public void setFlagMC(boolean b) {
		g_isMC=b;
	}


	public double getMCUtil(){
		double loutil=0;
		double hiutil=0;
		for(Task t:g_tasks){
			loutil+=(double)(t.c_l)/t.period;
		}
		for(Task t:g_tasks){
			if(t.is_HI)
				hiutil+=(double)(t.c_h)/t.period;
		}
		return Math.max(loutil, hiutil);
	}


	
	public void generate() {
		while(true){
			g_tasks=new Vector<Task>();
			genMC();
			if(g_param.check(getMCUtil())==1) break;
		}
	}
//	private int checkUtil()
//	{
//		double loutil=0;
//		double hiutil=0;
//		for(Task t:g_tasks){
//			loutil+=(double)(t.c_l)/t.period;
//		}
//		if(loutil==0) return 0;
//		for(Task t:g_tasks){
//			if(t.is_HI)
//				hiutil+=(double)(t.c_h)/t.period;
//		}
//		if(hiutil==0) return 0;
//		return 1;
//	}
	private void genMC()
	{
		int tid=0;
		Task t;
		while(getMCUtil()<=g_param.u_ub){
			t=genMCTask(tid);
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	
//	public Task genTask(int tid){
//		Task tsk=g_param.genTask(tid);
//		if(!g_param.chkTask(tsk))
//			return null;
//		return tsk;
//	}

	public Task genMCTask(int tid){
		Task tsk=g_param.genMCTask(tid);
		if(!g_param.chkTask(tsk))
			return null;
		if(!g_param.chkMCTask(tsk))
			return null;
		return tsk;
	}


	public void prn(int lv) {
		for(Task t:g_tasks)
		{
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+
					", h:"+t.c_h+", Xi:"+t.is_HI);
		}
		Log.prn(lv, "MC util:"+getMCUtil());
			
	}


	public Vector<Task> getAll() {
		return g_tasks;
	}


	public int size() {
		return g_tasks.size();
	}

	public void writeFile(String file) {
		TaskGenFile.writeFile(file, g_tasks);
	}
	
	public void loadFile(String f) {
		g_tasks=TaskGenFile.loadFile(f);
	}

	public void setUtil(double l, double u) {
		g_param.setUtil(l, u);
	}

	public void setTUtil(double l, double u) {
		g_param.setTUtil(l, u);
	}

	public void setRatioLH(double l, double u) {
		g_param.setRatioLH(l, u);
	}

	public void setPeriod(int l, int u) {
		g_param.setPeriod(l, u);
	}

	public void setProbHI(double p){
		g_param.prob_HI=p;
	}

	public int check(){
		return g_param.check(getMCUtil());
	}
}
