package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

import Util.Log;

public class TaskGen {
	private TaskGenParam g_param;
	private Vector<Task> g_tasks;
	private boolean isMC=false;
	
	public TaskGen() {
		g_param=new TaskGenParam();
	}

	public void switch_to_MC() {
		isMC=true;
	}

	public double getUtil(){
		double util=0;
		for(Task t:g_tasks){
			util+=(double)(t.c_h)/t.period;
		}
		return util;
	}


	
	public void generate() {
		while(true){
			g_tasks=new Vector<Task>();
			gen();
			if(g_param.check(getUtil())==1) break;
		}
	}
	private void gen()
	{
		int tid=0;
		while(getUtil()<=g_param.u_ub){
			Task t=genTask(tid);
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
		
	}
	
	public Task genTask(int tid){
		return g_param.genTask(tid);
	}

	public Task genMCTask(int tid){
		return g_param.genTask(tid);
	}



	public void prn(int lv) {
		for(Task t:g_tasks)
		{
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.c_l);
		}
		Log.prn(lv, "util:"+getUtil());
		
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

	public boolean chkTask(Task t) {
		return g_param.chkTask(t);
	}

	public int check(){
		return g_param.check(getUtil());
	}
}
