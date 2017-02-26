package testExp;

import gen.ConfigGen;
import gen.SimGen;
import gen.SimGenTM;
import anal.AnalEDF_AD_E;
import basic.TaskMng;
import simul.TaskSimul_EDF_AD;
import sysEx.TS_MC1;
import util.Log;
import util.TEngine;

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
		AnalEDF_AD_E a=new AnalEDF_AD_E();
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
		if(!a.isScheduable()){
			Log.prn(2, "not schedulable");
			return;
		}
		tm.setX(a.computeX());
		
		tm.getInfo().setProb_ms(0.4); // set prob
//		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		TaskSimul_EDF_AD ts=new TaskSimul_EDF_AD(tm);
		ts.isSchTab=false;
//		ts.isPrnMS=false;
		ts.isPrnEnd=false;
		ts.simulEnd(0,et);
		Log.prn(1,"DMR:"+ts.getSI().getDMR());
	}
	public int test2() 
	{
		ConfigGen cfg=new ConfigGen("exp/cfg/cfg_3.txt");
		cfg.readFile();
		SimGen eg=new SimGenTM(cfg);
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
