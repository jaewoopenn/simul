package Simul;

import java.util.Random;
import java.util.Vector;

import Util.Log;

public class TaskGen {
	private Random g_rand;
	private Vector<Task> g_tasks;
	private double g_util;
	private double g_tu_ub;
	private double g_tu_lb;
	private int g_p_ub;
	private int g_p_lb;
	public TaskGen() {
		g_tasks=new Vector<Task>();
		g_rand=new Random();
	}


	public double getUtil(){
		double util=0;
		for(Task t:g_tasks){
			util+=(double)(t.exec)/t.period;
		}
		return util;
	}


	public void setUtil(double d) {
		if(d>1){
			System.out.println("Error setUtil");
		}
		g_util=d;
	}


	public void setTUtil(double l, double u) {
		if(l>u){
			System.out.println("Error setTUtil");
		}
		g_tu_ub=u;
		g_tu_lb=l;
	}


	public void setPeriod(int l, int u) {
		if(l>u){
			System.out.println("Error setPeriod");
		}
		g_p_ub=u;
		g_p_lb=l;
		
	}
	public void generate() {
		while(getUtil()<=g_util){
			Task t=genTask();
			g_tasks.add(t);
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	public Task genTask(){
		int p=g_rand.nextInt(g_p_ub-g_p_lb)+g_p_lb;
		double tu=g_rand.nextDouble()*(g_tu_ub-g_tu_lb)+g_tu_ub;
		int e=(int)(tu*p);
		return new Task(0,p,e);
	}


	public boolean chkTask(Task t) {
		int p=t.period;
		if(p>=g_p_ub && p<g_p_lb)
			return false;
		double tu=(double)(t.exec)/t.period;
		if(tu>=g_p_ub && tu<g_p_lb)
			return false;
		return true;
	}


	public int check() {
		if(getUtil()>g_util)
			return 0;
		return 1;
	}


	public void prn() {
		for(Task t:g_tasks)
		{
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.exec);
		}
		Log.prn(1, "util:"+getUtil());
		
	}


	public Vector<Task> getAll() {
		return g_tasks;
	}


	public int size() {
		return g_tasks.size();
	}

}
