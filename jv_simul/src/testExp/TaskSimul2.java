package testExp;

import basic.Task;
import basic.TaskMng;
import simul.TaskSimul_EDF_AD_E;
import sysEx.TS_MC1;
import util.TEngine;

// MC // manual mode-switch
public class TaskSimul2 {
	public static int idx=7;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// MC
	public int test1()	{
		TaskMng tm=TS_MC1.ts1();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.getTM().prn();
		return 1;
	}
	public int test2() {
		int et=20;
		TaskMng tm=TS_MC1.ts1();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulEnd(0,et);
		return 0;
	}
	public int test3()	{
		int et=16;
		TaskMng tm=TS_MC1.ts2();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulEnd(0,et);
		return 0;
	}
	public int test4()	{
		int et=12;
		TaskMng tm=TS_MC1.ts1();
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulBy(0, 3);
		ts.mode_switch(tm.getTask(1));
		ts.simulEnd(0,et);
		return 0;
	}
	
	public  int test5() {
		int et=12;
		TaskMng tm=TS_MC1.ts1();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulBy(0, 1);
		ts.mode_switch(tm.getTask(1));
		ts.simulEnd(1,et);
		return 0;
	}
	public  int test6()	{
		int et=12;
		TaskMng tm=TS_MC1.ts1();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulBy(0, 1);
		ts.mode_switch(tm.getTask(1));
		Task t=tm.getTask(0);
		ts.dropTask_base(t);
		ts.simulEnd(1,et);
		return 0;
	}
	public  int test7()	{
		int et=24;
		TaskMng tm=TS_MC1.ts3();
		tm.setX(0.5);
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulBy(0, 1);
		ts.mode_switch(tm.getTask(2));
//		ts.drop(tm.getTask(0));
		ts.dropTask_base(tm.getTask(1));
		ts.simulEnd(1,et);
		return 0;
	}
	public  int test8()	{
		return -1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}


	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = TaskSimul2.class;
		TaskSimul2 m=new TaskSimul2();
		int[] aret=TaskSimul2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
