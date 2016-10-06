package testExp;

import basic.TaskMng;
import exp.AlgoEDF_AT_S;
import exp.TaskSimul;
import simul.AnalEDF_AT;
import simul.AnalEDF_AT_S;
import simul.ConfigGen;
import simul.SimGen;
import taskSetEx.TS_MC1;
import utilSim.Log;
import utilSim.TEngine;

// EDF-AT-S
public class TaskSimul4 {
	public static int idx=2;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// EDF-AT-S
	public int test1() 
	{
		TaskMng tm=TS_MC1.ts3();
		exec(tm,300);
		return -1;
	}
	public void exec(TaskMng tm, int et){
		AnalEDF_AT_S a=new AnalEDF_AT_S();
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
		if(!a.isScheduable()){
			Log.prn(2, "not schedulable");
			return;
		}
		tm.setX(a.getX());
		
		tm.getInfo().setProb_ms(0.1); // set prob
		TaskSimul ts=new TaskSimul(tm,new AlgoEDF_AT_S());
		ts.isSchTab=false;
		ts.isPrnMS=false;
		ts.isPrnEnd=false;
		ts.simulEnd(0,et);
//		Log.prn(1,"DMR:"+ts.getDMR());
	}
	public int test2() 
	{
		ConfigGen cfg=new ConfigGen("exp/cfg/cfg_3.txt");
		cfg.readFile();
		SimGen eg=new SimGen(cfg);
		eg.prepare();
		TaskMng tm=eg.genOne();
		exec(tm,1000);
		return 0;
	}
	public int test3() 
	{
		return 0;
	}
	public  int test4() 
	{
		return 0;
	}
	public  int test5() 
	{
		return 0;
	}
	public  int test6() 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}


	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSimul4.class;
		TaskSimul4 m=new TaskSimul4();
		int[] aret=TaskSimul4.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
