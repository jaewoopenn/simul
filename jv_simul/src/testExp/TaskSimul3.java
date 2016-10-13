package testExp;

import anal.AnalEDF_AT;
import anal.AnalEDF_VD;
import anal.ConfigGen;
import anal.SimGen;
import basic.TaskMng;
import simul.TaskSimul_EDF_AT;
import simul.TaskSimul_EDF_AT_S;
import simul.TaskSimul_EDF_VD;
import taskSetEx.TS_MC1;
import utilSim.Log;
import utilSim.TEngine;

// MC 
public class TaskSimul3 {
	public static int idx=1;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// MC
	public int test1()	{
		int et=24;
		TaskMng tm=TS_MC1.ts3();
		tm.setX(0.5);
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		ts.simulBy(0, 2);
		ts.modeswitch(3);
		ts.simulEnd(2,et);
		return -1;
	}
	public int test2() {
		int et=36;
		TaskMng tm=TS_MC1.ts3();
		tm.getInfo().setProb_ms(0.6);
		tm.setX(1.0/3);
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		ts.simulEnd(0,et);
		return -1;
	}
	public int test3()	{
		int et=48;
		TaskMng tm=TS_MC1.ts3();
		tm.getInfo().setProb_ms(0.4); // set prob
		tm.setX(1.0/3);
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		ts.simulEnd(0,et);
		return -1;
	}
	public int test4()	{
		TaskMng tm=TS_MC1.ts5();
		exec(tm,48);
		return -1;
	}
	public void exec(TaskMng tm, int et){
//		AnalEDF_AT_S a=new AnalEDF_AT_S();
		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
		if(!a.isScheduable()){
			Log.prn(2, "not schedulable");
			return;
		}
		tm.setX(a.getX());
		
		tm.getInfo().setProb_ms(0.1); // set prob
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		ts.isSchTab=false;
//		ts.isPrnMS=false;
		ts.simulEnd(0,et);
		Log.prn(1,"DMR:"+ts.getSI().getDMR());
	}
	
	public  int test5() {
		ConfigGen cfg=new ConfigGen("exp/cfg/cfg_3.txt");
		cfg.readFile();
		SimGen eg=new SimGen(cfg);
		eg.prepare();
		TaskMng tm=eg.genOne();
		exec(tm,1000);
		return -1;
	}
	public  int test6()	{
		TaskMng tm=TS_MC1.ts3();
		exec(tm,300);
		return -1;
	}
	public  int test7()	{
		int et=100;
		TaskMng tm=TS_MC1.ts3();
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		if(!a.isScheduable()){
			Log.prn(2, "not schedulable");
			return 1;
		}
		tm.setX(a.getX());
		
		tm.getInfo().setProb_ms(0.1); // set prob
		TaskSimul_EDF_VD ts=new TaskSimul_EDF_VD(tm);
//		ts.isSchTab=false;
//		ts.isPrnMS=false;
		ts.simulEnd(0,et);
		Log.prn(1,"DMR:"+ts.getSI().getDMR());
		return 1;
	}
	public  int test8()	{
		int et=1000;
		TaskMng tm=TS_MC1.ts3();
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		tm.setX(a.getX());
		
		tm.getInfo().setProb_ms(0.1); // set prob
		TaskSimul_EDF_AT ts=new TaskSimul_EDF_AT(tm);
//		ts.isSchTab=false;
//		ts.isPrnMS=false;
		ts.simulEnd(0,et);
		Log.prn(1,"DMR:"+ts.getSI().getDMR());
		return 1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}


	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSimul3.class;
		TaskSimul3 m=new TaskSimul3();
		int[] aret=TaskSimul3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
