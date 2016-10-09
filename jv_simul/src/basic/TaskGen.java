package basic;

import java.util.Vector;

import utilSim.Log;

public abstract class TaskGen {
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
			if (!t.check())
				continue;
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	

	public abstract Task genTask(int tid);

	public void assignComp(int max){
//		for(Task t:g_tasks) {
//			t.setCom(g_param.getComp(max));
//		}
		
	}

	public int check(){
		return g_param.check(getUtil());
	}
	
	public abstract void prn(int lv) ;


	protected abstract double getUtil(); 

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
		TaskFile.writeFile(file, g_tasks);
	}
	
//	public void loadFile(String f) {
//		g_tasks=TaskFile.loadFile(f);
//	}
//	
//
//	public TaskMng loadFileTM(String f) {
//		loadFile(f);
//		TaskMngPre tm=new TaskMngPre();
//		tm.setTasks(getAll());
//		TaskMng m=tm.freezeTasks();
//		return m;
//	}
	
}
