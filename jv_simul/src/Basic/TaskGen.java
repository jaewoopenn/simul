package Basic;

import java.util.Vector;

import Util.Log;

public class TaskGen {
	private TaskGenParam g_param;
	private Vector<Task> g_tasks;
	
	public TaskGen() {
		g_param=new TaskGenParam();
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
		Task tsk=g_param.genMCTask(tid);
		if(!g_param.chkTask(tsk))
			return null;
		if(!g_param.chkMCTask(tsk))
			return null;
		return tsk;
	}

	public void assignComp(int max){
		for(Task t:g_tasks) {
			t.setCom(g_param.getComp(max));
		}
		
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
	
	// setting 
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

	
	// file
	public void writeFile(String file) {
		TaskGenFile.writeFile(file, g_tasks);
	}
	
	public void loadFile(String f) {
		g_tasks=TaskGenFile.loadFile(f);
	}
	public void writeFile2(String file) {
		TaskGenFile.writeFile2(file, g_tasks);
	}
	
	public void loadFile2(String f) {
		g_tasks=TaskGenFile.loadFile2(f);
	}
	
}
