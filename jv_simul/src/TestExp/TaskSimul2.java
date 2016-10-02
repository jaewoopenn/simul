package TestExp;

import Util.TEngine;
import Basic.Task;
import Basic.TaskGen;
import Basic.TaskMng;
import Basic.TaskMngPre;
import Exp.TaskSimul;

// MC
public class TaskSimul2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={1,1,1,0,-1, -1,-1,-1,-1,-1};

	// MC
	public int test1()	{
		TaskSimul ts=new TaskSimul(ts1());
		ts.prepareMC();
		ts.prn();
		return 1;
	}
	public int test2() {
		TaskSimul ts=new TaskSimul(ts1());
		ts.prepareMC();
		ts.init();
		ts.simulDur(0, 20);
		return ts.simulEnd(20);
	}
	
	public  int test3()	{
		int et=40;
		TaskGen tg=new TaskGen();
		TaskMng tm=tg.loadFileTM("exp/ts/test1.txt");
		TaskSimul ts=new TaskSimul(tm);
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

	public TaskMng ts1()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,6,1,5));
		tm.add(new Task(0,4,1));
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
	public TaskMng ts2()	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,3));
		TaskMng m=tm.freezeTasks();
		return m;
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
