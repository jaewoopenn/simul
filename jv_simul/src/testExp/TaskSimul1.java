package testExp;

import basic.TaskMng;
import basic.TaskSetFix;
import simul.TaskSimul_EDF_AD_E;
import sysEx.TS_NonMC1;
import util.TEngine;

public class TaskSimul1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=4;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(TS_NonMC1.ts5());
		
		ts.simulEnd(0,20);
		return 0;
	}
	public int test2() {
		int et=40;
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(TS_NonMC1.ts2());
		ts.simulEnd(0,et);
		return 0;
	}
	
	public  int test3()	{
		int et=40;
		TaskSetFix tmp=TaskSetFix.loadFile("test/ts/simul1.txt");
		TaskMng tm=tmp.getTM();
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulEnd(0,et);
		return 0;
	}
	
	public  int test4()	{
		int et=80;
		TaskSetFix tmp=TaskSetFix.loadFile("test/ts/simul1.txt");
		TaskMng tm=tmp.getTM();
		TaskSimul_EDF_AD_E ts=new TaskSimul_EDF_AD_E(tm);
		ts.simulEnd(0,et);
		return 0;
	}
	
	public  int test5() {
		return -1;
	}
	public  int test6()	{
		return -1;
	}
	public  int test7()	{
		return -1;
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
		Class c = TaskSimul1.class;
		TaskSimul1 m=new TaskSimul1();
		int[] aret=TaskSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
