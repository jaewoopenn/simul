package testExp;

import basic.TaskMng;
import exp.TaskSimul;
import taskSetEx.TS_MC1;
import utilSim.TEngine;

// MC 
public class TaskSimul3 {
	public static int idx=2;
//	public static int idx=-1;
	public static int log_level=1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	// MC
	public int test1()	{
		int et=24;
		TaskMng tm=TS_MC1.ts3();
		tm.setX(0.5);
		TaskSimul ts=new TaskSimul(tm);
		ts.simulBy(0, 2);
		ts.modeswitch(3);
		ts.simulEnd(2,et);
		return -1;
	}
	public int test2() {
		int et=24;
		TaskMng tm=TS_MC1.ts3();
		tm.setX(0.5);
		TaskSimul ts=new TaskSimul(tm);
		ts.simulEnd(0,et);
		return -1;
	}
	public int test3()	{
		return -1;
	}
	public int test4()	{
		return -1;
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
		Class c = TaskSimul3.class;
		TaskSimul3 m=new TaskSimul3();
		int[] aret=TaskSimul3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
