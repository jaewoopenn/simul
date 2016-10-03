package testExp;

import basic.TaskGen;
import basic.TaskMng;
import exp.TaskSimul;
import taskSetEx.MC1;
import utilSim.TEngine;

// MC
public class TaskSimul2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int gret[]={1,1,1,0,-1, -1,-1,-1,-1,-1};

	// MC
	public int test1()	{
		TaskSimul ts=new TaskSimul(MC1.ts1());
		ts.getTM().setX(0.5);
		ts.getTM().prn();
		return 1;
	}
	public int test2() {
		int et=20;
		TaskSimul ts=new TaskSimul(MC1.ts1());
		ts.getTM().setX(0.5);
		ts.init();
		ts.simulDur(0, et);
		return ts.simulEnd(et);
	}
	
	public  int test3()	{
		int et=16;
		TaskSimul ts=new TaskSimul(MC1.ts2());
		ts.getTM().setX(0.5);
		ts.init();
		ts.simulDur(0, et);
		return ts.simulEnd(et);
	}
	
	public  int test4()	{
		int et=80;
		TaskGen tg=new TaskGen();
		TaskMng tm=tg.loadFileTM("exp/ts/test2.txt");
//		tm.prn();
		TaskSimul ts=new TaskSimul(tm);
		return ts.exec(et);
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
		Class c = TaskSimul2.class;
		TaskSimul2 m=new TaskSimul2();
		int[] aret=TaskSimul2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
