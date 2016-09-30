package Basic;

import java.util.Vector;

import Util.Log;

public class TaskGen {
	protected TaskGenParam g_param;
	protected Vector<Task> g_tasks;
	public TaskGen() {
		g_param=new TaskGenParam();
	}

	public void generate() {
		while(true){
			g_tasks=new Vector<Task>();
			genTaskSet();
			if(g_param.check(getUtil())==1) break;
		}
	}
	private void genTaskSet()
	{
		int tid=0;
		Task t;
		while(getUtil()<=g_param.u_ub){
			t=genTask(tid);
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	

	public Task genTask(int tid){
		Task tsk=g_param.genTask(tid);
		if(!g_param.chkTask(tsk))
			return null;
		return tsk;
	}

	public void assignComp(int max){
		for(Task t:g_tasks) {
			t.setCom(g_param.getComp(max));
		}
		
	}

	public int check(){
		return g_param.check(getUtil());
	}
	
	// getting
	public void prn(int lv) {
		for(Task t:g_tasks) {
			Log.prn(1, "tid:"+t.tid+", p:"+t.period+", l:"+t.c_l);
		}
		Log.prn(lv, "Util:"+getUtil());
			
	}


	public double getUtil(){
		double util=0;
		for(Task t:g_tasks){
			util+=(double)(t.c_l)/t.period;
		}
		return util;
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

	public TaskMng loadFileTM(String f) {
		loadFile(f);
		TaskMng tm=new TaskMng();
		tm.setTasks(getAll());
		tm.freezeTasks();
		return tm;
	}
	
}
