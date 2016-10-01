package TestExp;

// MC
import Util.TEngine;
import Basic.TaskGen;
import Basic.TaskMng;
import Exp.JobMng;
import Exp.JobSimul;
import Exp.TaskSimul;

public class TaskSimul2 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=4;
	public static int gret[]={1,0,1,0,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		TaskSimul ts=new TaskSimul(ts1());
		ts.init();
		ts.simulDur(0, 20);
		return ts.simulEnd(20);
	}
	public int test2() {
		int et=40;
		TaskSimul ts=new TaskSimul(ts2());
		ts.init();
		ts.simulDur(0, et);
		return ts.simulEnd(et);
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
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,2);
		tm.freezeTasks();
//		tm.prn();
		return tm;
	}
	
	public TaskMng ts2()	{
		TaskMng tm=new TaskMng();
		tm.addTask(3,1);
		tm.addTask(4,3);
		tm.freezeTasks();
		return tm;
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
