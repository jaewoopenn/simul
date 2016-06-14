package Simul;

import java.util.Vector;

import Util.Log;

public class CompGen {
	private CompGenParam g_param;
	private Vector<Task> g_tasks;
	
	public CompGen(CompGenParam param) {
		g_param=param;
	}

	public void generate() {
		while(true){
			g_tasks=new Vector<Task>();
			genTaskSet();
			if(g_param.check(getMCUtil())==1) break;
		}
	}
	private void genTaskSet()
	{
		int tid=0;
		Task t;
		while(getMCUtil()<=g_param.u_ub){
			t=genTask(tid);
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	

	public Task genTask(int tid){
		Task tsk=g_param.genComp(tid);
		if(!g_param.chkTask(tsk))
			return null;
		if(!g_param.chkMCTask(tsk))
			return null;
		return tsk;
	}


	public int check(){
		return g_param.check(getMCUtil());
	}
	
	// getting
	public void prn(int lv) {
		for(Task t:g_tasks) {
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+
					", h:"+t.c_h+", Xi:"+t.is_HI);
		}
		Log.prn(lv, "MC util:"+getMCUtil());
			
	}

	public void prn2(int lv) {
		for(Task t:g_tasks) {
			Log.prn(lv, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l+
					", h:"+t.c_h+", Xi:"+t.is_HI+", com:"+t.cid);
		}
		Log.prn(lv, "MC util:"+getMCUtil());
			
	}

	
	public double getMCUtil(){
		double loutil=0;
		double hiutil=0;
		for(Task t:g_tasks){
			loutil+=(double)(t.c_l)/t.period;
			if(t.is_HI)
				hiutil+=(double)(t.c_h)/t.period;
		}
		return Math.max(loutil, hiutil);
	}

	public Vector<Task> getAll() {
		return g_tasks;
	}


	public int size() {
		return g_tasks.size();
	}
	
	
	// file
	public void writeFile(String file) {
		CompGenFile.writeFile(file, g_tasks);
	}
	
	public void loadFile(String f) {
		g_tasks=CompGenFile.loadFile(f);
	}
	
}
